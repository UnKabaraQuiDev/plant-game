import bpy
import os
import json
from mathutils import Vector, Matrix
import sys
from math import radians

# Blender → OpenGL axis correction
blender_to_opengl_mat = Matrix.Rotation(radians(-90), 4, 'X')

DATA_EXPORT_DIR = bpy.path.abspath("//export/")
MODELS_EXPORT_DIR = bpy.path.abspath("//export/models/")
MODELS_REL_DIR = os.path.relpath(MODELS_EXPORT_DIR, DATA_EXPORT_DIR)
TEXTURES_EXPORT_DIR = bpy.path.abspath("//export/textures/")
TEXTURES_REL_DIR = os.path.relpath(TEXTURES_EXPORT_DIR, DATA_EXPORT_DIR)


class StdOutOverride:
    def write(self, text):
        sys.__stdout__.write(text)
        if text != '\n':
            for line in text.replace('\t', ' ').split('\n'):
                for area in bpy.context.screen.areas:
                    if area.type == 'CONSOLE':
                        with bpy.context.temp_override(area=area):
                            bpy.ops.console.scrollback_append(text=line, type='OUTPUT')


sys.stdout = StdOutOverride()


def export_obj(objects, filepath):
    bpy.ops.object.select_all(action='DESELECT')
    for obj in objects:
        obj.select_set(True)

    bpy.ops.wm.obj_export(
        filepath=filepath,
        apply_modifiers=True,
        export_triangulated_mesh=True,
        export_materials=False,
        export_colors=False,
        export_selected_objects=True
    )

    bpy.ops.object.select_all(action='DESELECT')


def export_mat(obj):
    for mat_slot in obj.material_slots:
        mat = mat_slot.material
        if not mat or not mat.use_nodes:
            continue

        for node in mat.node_tree.nodes:
            if node.type == 'TEX_IMAGE' and node.image:
                image = node.image
                export_path = os.path.join(TEXTURES_EXPORT_DIR, f"{image.name}.png")
                image.filepath_raw = export_path
                image.file_format = 'PNG'
                image.save()
                return f"{image.name}.png"
    return None


def get_object_by_prefix(collection, prefix):
    for obj in collection.objects:
        if obj.name.lower().startswith(prefix.lower()) and not obj.hide_render:
            return obj
    return None


def matrix_to_trs(matrix):
    loc, rot, scale = matrix.decompose()
    return loc, rot, scale


os.makedirs(MODELS_EXPORT_DIR, exist_ok=True)
os.makedirs(TEXTURES_EXPORT_DIR, exist_ok=True)


for collection in bpy.data.collections:
    print(f"Processing collection: {collection.name}")

    collection_data = {
        "collection_name": collection.name,
        "animated": False,
        "meshes": {}
    }

    wasEnabled = bpy.context.view_layer.layer_collection.children[collection.name].exclude
    bpy.context.view_layer.layer_collection.children[collection.name].exclude = False

    origin = get_object_by_prefix(collection, "origin")
    animated = get_object_by_prefix(collection, "animated")
    start_empty = get_object_by_prefix(collection, "start")
    end_empty = get_object_by_prefix(collection, "end")

    if origin is None:
        print(f"  No origin in {collection.name}, skipping.")
        bpy.context.view_layer.layer_collection.children[collection.name].exclude = wasEnabled
        continue

    # =========================================================
    # EXPORT SPACE BASE (CRITICAL FIX)
    # Everything is expressed relative to this matrix
    # =========================================================
    export_space = origin.matrix_world.copy()
    export_inv = export_space.inverted()

    static_objs = [
        obj for obj in collection.objects
        if not obj.name.lower().startswith("animated") and not obj.hide_render
    ]

    static_export_path = os.path.join(MODELS_EXPORT_DIR, f"{collection.name}_static.obj")

    txt_name = next(
        (export_mat(x) for x in static_objs if export_mat(x) is not None),
        None
    )

    export_obj(static_objs, static_export_path)

    # STATIC OFFSET (correct space)
    static_offset = export_inv @ origin.matrix_world
    static_loc, _, _ = static_offset.decompose()

    collection_data["meshes"]["static"] = {
        "file": os.path.join(MODELS_REL_DIR, os.path.basename(static_export_path)),
        "offset_from_origin": [static_loc.x, static_loc.y, static_loc.z],
        "texture_material": True if txt_name else False,
        "texture_path": os.path.join(TEXTURES_REL_DIR, txt_name) if txt_name else None
    }

    # =========================================================
    # ANIMATED MESH
    # =========================================================
    if animated:
        collection_data["animated"] = True

        animated_export_path = os.path.join(MODELS_EXPORT_DIR, f"{collection.name}_animated.obj")
        export_obj([animated], animated_export_path)

        animated_world = export_inv @ animated.matrix_world
        anim_loc, _, _ = animated_world.decompose()

        animated_entry = {
            "file": os.path.join(MODELS_REL_DIR, os.path.basename(animated_export_path)),
            "offset_from_origin": [anim_loc.x, anim_loc.y, anim_loc.z],
            "texture_material": True if txt_name else False,
            "texture_path": os.path.join(TEXTURES_REL_DIR, txt_name) if txt_name else None
        }

        # =========================================================
        # ANIMATION (FULL FIXED PIPELINE)
        # =========================================================
        if start_empty and end_empty:

            start_mat = export_inv @ start_empty.matrix_world
            end_mat = export_inv @ end_empty.matrix_world

            # IMPORTANT: axis conversion AFTER space normalization
            start_mat = blender_to_opengl_mat @ start_mat
            end_mat = blender_to_opengl_mat @ end_mat

            start_loc, start_rot, start_scale = matrix_to_trs(start_mat)
            end_loc, end_rot, end_scale = matrix_to_trs(end_mat)

            animated_entry["animation"] = {
                "start_position": [start_loc.x, start_loc.y, start_loc.z],
                "end_position": [end_loc.x, end_loc.y, end_loc.z],
                "start_rotation": [start_rot.x, start_rot.y, start_rot.z, start_rot.w],
                "end_rotation": [end_rot.x, end_rot.y, end_rot.z, end_rot.w],
                "start_scale": [start_scale.x, start_scale.y, start_scale.z],
                "end_scale": [end_scale.x, end_scale.y, end_scale.z]
            }

        collection_data["meshes"]["animated"] = animated_entry

    # =========================================================
    # WRITE FILE
    # =========================================================
    json_path = os.path.join(DATA_EXPORT_DIR, f"{collection.name}.json")
    with open(json_path, "w", encoding="utf-8") as f:
        json.dump(collection_data, f, indent=4)

    print(f"Exported data for {collection.name} -> {json_path}")

    bpy.context.view_layer.layer_collection.children[collection.name].exclude = wasEnabled