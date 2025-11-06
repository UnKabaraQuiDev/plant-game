import bpy
import os
import json
from mathutils import Vector, Matrix
import sys
from math import radians

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
    """Exports the first found texture image of a material and returns its filename."""
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
                print(f"Exported {image.name} to {export_path}")
                return f"{image.name}.png"
    return None

def get_object_by_prefix(collection, prefix):
    for obj in collection.objects:
        if obj.name.lower().startswith(prefix.lower()) and not obj.hide_render:
            return obj
    return None

def matrix_to_trs(matrix):
    # loc, rot, scale = (blender_to_opengl_mat @ matrix).decompose()
    loc, rot, scale = (matrix).decompose()
    return loc, rot, scale

# Ensure export folders exist
os.makedirs(MODELS_EXPORT_DIR, exist_ok=True)
os.makedirs(TEXTURES_EXPORT_DIR, exist_ok=True)

for collection in bpy.data.collections:
    print(f"Processing collection: {collection.name}")

    # Prepare JSON structure
    collection_data = {
        "collection_name": collection.name,
        "animated": False,
        "meshes": {}
    }

    wasEnabled = bpy.context.view_layer.layer_collection.children[collection.name].exclude
    bpy.context.view_layer.layer_collection.children[collection.name].exclude = False

    # Identify objects
    origin = get_object_by_prefix(collection, "origin")
    animated = get_object_by_prefix(collection, "animated")
    start_empty = get_object_by_prefix(collection, "start")
    end_empty = get_object_by_prefix(collection, "end")
    
    if origin is None:
        print(f"  No origin object in collection {collection.name}, skipping.")
        bpy.context.view_layer.layer_collection.children[collection.name].exclude = wasEnabled
        continue

    original_origin_loc = origin.location.copy()

    # Parent all objects to origin
    for obj in collection.objects:
        if obj != origin:
            obj.select_set(True)
    origin.select_set(True)
    bpy.context.view_layer.objects.active = origin
    bpy.ops.object.parent_set(type='OBJECT', keep_transform=True)
    bpy.ops.object.select_all(action='DESELECT')

    # Move to world origin
    origin.location = Vector((0, 0, 0))
    bpy.context.view_layer.update()

    # Export static mesh
    static_objs = [obj for obj in collection.objects if not obj.name.lower().startswith("animated") and not obj.hide_render]
    static_export_path = os.path.join(MODELS_EXPORT_DIR, f"{collection.name}_static.obj")
    txt_name = next((export_mat(x) for x in static_objs if export_mat(x) is not None), None)
    export_obj(static_objs, static_export_path)

    static_entry = {
        "file": os.path.join(MODELS_REL_DIR, os.path.basename(static_export_path)),
        "offset_from_origin": [0.0, 0.0, 0.0],
        "texture_material": True if txt_name else False,
        "texture_path": os.path.join(TEXTURES_REL_DIR, txt_name) if txt_name else None
    }
    collection_data["meshes"]["static"] = static_entry

    # Export animated mesh if it exists
    if animated:
        collection_data["animated"] = True
        animated_export_path = os.path.join(MODELS_EXPORT_DIR, f"{collection.name}_animated.obj")
        export_obj([animated], animated_export_path)
        offset = animated.location - origin.location

        animated_entry = {
            "file": os.path.join(MODELS_REL_DIR, os.path.basename(animated_export_path)),
            "offset_from_origin": [offset.x, offset.y, offset.z],
            "texture_material": True if txt_name else False,
            "texture_path": os.path.join(TEXTURES_REL_DIR, txt_name) if txt_name else None
        }

        if start_empty and end_empty:
            start_loc, start_rot, start_scale = matrix_to_trs(start_empty.matrix_world)
            end_loc, end_rot, end_scale = matrix_to_trs(end_empty.matrix_world)
            animated_entry["animation"] = {
                "start_position": [start_loc.x, start_loc.y, start_loc.z],
                "end_position": [end_loc.x, end_loc.y, end_loc.z],
                "start_rotation": [start_rot.x, start_rot.y, start_rot.z, start_rot.w],
                "end_rotation": [end_rot.x, end_rot.y, end_rot.z, end_rot.w],
                "start_scale": [start_scale.x, start_scale.y, start_scale.z],
                "end_scale": [end_scale.x, end_scale.y, end_scale.z]
            }

        collection_data["meshes"]["animated"] = animated_entry

    # Write final JSON
    json_path = os.path.join(DATA_EXPORT_DIR, f"{collection.name}.json")
    with open(json_path, "w", encoding="utf-8") as f:
        json.dump(collection_data, f, indent=4)
    print(f"Exported data for {collection.name} -> {json_path}")

    bpy.context.view_layer.layer_collection.children[collection.name].exclude = wasEnabled
