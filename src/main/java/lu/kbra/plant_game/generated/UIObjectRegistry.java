// @formatter:off
package lu.kbra.plant_game.generated;

import java.lang.Class;
import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.BackButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.LevelButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.LevelState;
import lu.kbra.plant_game.engine.entity.ui.btn.MoneyUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.OptionsButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.PlayButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.QuitButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutScrollDrivenUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.OffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.ScrollContainerUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.ScrollDrivenUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.impl.Scale2dDir;
import lu.kbra.plant_game.engine.entity.ui.layout.SpacerUIObject;
import lu.kbra.plant_game.engine.entity.ui.prim.MeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.prim.QuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.scroller.FlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.scroller.ScrollBarUIObject;
import lu.kbra.plant_game.engine.entity.ui.slider.SliderUIObject;
import lu.kbra.plant_game.engine.entity.ui.slider.VolumeSliderUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.DelegatingTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.GrowOnHoverTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.OptionKeyUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticGrowOnHoverTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.TextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.VolumeTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.textinput.TextBoxUIObject;
import lu.kbra.plant_game.engine.entity.ui.textinput.TextFieldUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.CursorUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.GradientQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.LargeLogoUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.TextureUIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.render.GradientDirection;
import lu.kbra.plant_game.engine.render.GradientQuadMesh;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.plant_game.engine.util.InternalConstructorFunction;
import lu.kbra.plant_game.engine.util.exceptions.UIObjectConstructorNotFound;
import lu.kbra.plant_game.engine.util.exceptions.UIObjectNotFound;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.QuadMesh;
import lu.kbra.standalone.gameengine.objs.entity.Component;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;
import lu.pcy113.pclib.impl.TriConsumer;
import org.joml.Vector2f;
import org.joml.Vector3fc;
import org.joml.Vector4f;

public class UIObjectRegistry {
	private static final Map<Class<? extends UIObject>, List<InternalConstructorFunction<UIObject>>> UI_OBJECT_CONSTRUCTORS;

	static {
		UI_OBJECT_CONSTRUCTORS = new HashMap<>();

		/*                 ProgrammaticTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listProgrammaticTextUIObject = new ArrayList<>();
		listProgrammaticTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class}, (Object[] arr) -> (UIObject) new ProgrammaticTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2])));
		listProgrammaticTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, Transform3D.class}, (Object[] arr) -> (UIObject) new ProgrammaticTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (Transform3D) arr[3])));
		UI_OBJECT_CONSTRUCTORS.put(ProgrammaticTextUIObject.class, listProgrammaticTextUIObject);

		/*                 OptionsButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listOptionsButtonUIObject = new ArrayList<>();
		listOptionsButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new OptionsButtonUIObject((String) arr[0], (TextEmitter) arr[1])));
		listOptionsButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new OptionsButtonUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(OptionsButtonUIObject.class, listOptionsButtonUIObject);

		/*                 SliderUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listSliderUIObject = new ArrayList<>();
		listSliderUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, float.class, float.class, float.class, int.class}, (Object[] arr) -> (UIObject) new SliderUIObject((String) arr[0], (TextEmitter) arr[1], (float) arr[2], (float) arr[3], (float) arr[4], (int) arr[5])));
		listSliderUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class, float.class, float.class, float.class, int.class}, (Object[] arr) -> (UIObject) new SliderUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2], (float) arr[3], (float) arr[4], (float) arr[5], (int) arr[6])));
		UI_OBJECT_CONSTRUCTORS.put(SliderUIObject.class, listSliderUIObject);

		/*                 CursorUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listCursorUIObject = new ArrayList<>();
		listCursorUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new CursorUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		listCursorUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new CursorUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(CursorUIObject.class, listCursorUIObject);

		/*                 MeshUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listMeshUIObject = new ArrayList<>();
		listMeshUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (UIObject) new MeshUIObject((String) arr[0], (Mesh) arr[1])));
		listMeshUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new MeshUIObject((String) arr[0], (Mesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(MeshUIObject.class, listMeshUIObject);

		/*                 GrowOnHoverTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listGrowOnHoverTextUIObject = new ArrayList<>();
		listGrowOnHoverTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Scale2dDir.class}, (Object[] arr) -> (UIObject) new GrowOnHoverTextUIObject((String) arr[0], (TextEmitter) arr[1], (Scale2dDir) arr[2])));
		listGrowOnHoverTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Scale2dDir.class, Transform3D.class}, (Object[] arr) -> (UIObject) new GrowOnHoverTextUIObject((String) arr[0], (TextEmitter) arr[1], (Scale2dDir) arr[2], (Transform3D) arr[3])));
		UI_OBJECT_CONSTRUCTORS.put(GrowOnHoverTextUIObject.class, listGrowOnHoverTextUIObject);

		/*                 TextBoxUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listTextBoxUIObject = new ArrayList<>();
		listTextBoxUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new TextBoxUIObject((String) arr[0], (TextEmitter) arr[1])));
		listTextBoxUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new TextBoxUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(TextBoxUIObject.class, listTextBoxUIObject);

		/*                 QuitButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listQuitButtonUIObject = new ArrayList<>();
		listQuitButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new QuitButtonUIObject((String) arr[0], (TextEmitter) arr[1])));
		listQuitButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new QuitButtonUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(QuitButtonUIObject.class, listQuitButtonUIObject);

		/*                 MoneyUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listMoneyUIObject = new ArrayList<>();
		listMoneyUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new MoneyUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		listMoneyUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new MoneyUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(MoneyUIObject.class, listMoneyUIObject);

		/*                 PlayButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listPlayButtonUIObject = new ArrayList<>();
		listPlayButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new PlayButtonUIObject((String) arr[0], (TextEmitter) arr[1])));
		listPlayButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new PlayButtonUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(PlayButtonUIObject.class, listPlayButtonUIObject);

		/*                 UIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listUIObjectGroup = new ArrayList<>();
		listUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObject[].class}, (Object[] arr) -> (UIObject) new UIObjectGroup((String) arr[0], (UIObject[]) arr[1])));
		listUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Component[].class}, (Object[] arr) -> (UIObject) new UIObjectGroup((String) arr[0], (Component[]) arr[1])));
		listUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, List.class, Component[].class}, (Object[] arr) -> (UIObject) new UIObjectGroup((String) arr[0], (List) arr[1], (Component[]) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(UIObjectGroup.class, listUIObjectGroup);

		/*                 LayoutScrollDrivenUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listLayoutScrollDrivenUIObjectGroup = new ArrayList<>();
		listLayoutScrollDrivenUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Supplier.class, Direction.class, float.class, Layout.class, UIObject[].class}, (Object[] arr) -> (UIObject) new LayoutScrollDrivenUIObjectGroup((String) arr[0], (Supplier) arr[1], (Direction) arr[2], (float) arr[3], (Layout) arr[4], (UIObject[]) arr[5])));
		listLayoutScrollDrivenUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, Supplier.class, Direction.class, float.class, Layout.class, UIObject[].class}, (Object[] arr) -> (UIObject) new LayoutScrollDrivenUIObjectGroup((String) arr[0], (UIObjectGroup) arr[1], (Supplier) arr[2], (Direction) arr[3], (float) arr[4], (Layout) arr[5], (UIObject[]) arr[6])));
		listLayoutScrollDrivenUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Transform3D.class, Supplier.class, Direction.class, float.class, Layout.class, UIObject[].class}, (Object[] arr) -> (UIObject) new LayoutScrollDrivenUIObjectGroup((String) arr[0], (Transform3D) arr[1], (Supplier) arr[2], (Direction) arr[3], (float) arr[4], (Layout) arr[5], (UIObject[]) arr[6])));
		UI_OBJECT_CONSTRUCTORS.put(LayoutScrollDrivenUIObjectGroup.class, listLayoutScrollDrivenUIObjectGroup);

		/*                 TextFieldUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listTextFieldUIObject = new ArrayList<>();
		listTextFieldUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new TextFieldUIObject((String) arr[0], (TextEmitter) arr[1])));
		listTextFieldUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new TextFieldUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(TextFieldUIObject.class, listTextFieldUIObject);

		/*                 DelegatingTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listDelegatingTextUIObject = new ArrayList<>();
		listDelegatingTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, TriConsumer.class, TriConsumer.class}, (Object[] arr) -> (UIObject) new DelegatingTextUIObject((String) arr[0], (TextEmitter) arr[1], (TriConsumer) arr[2], (TriConsumer) arr[3])));
		listDelegatingTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class, TriConsumer.class, TriConsumer.class}, (Object[] arr) -> (UIObject) new DelegatingTextUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2], (TriConsumer) arr[3], (TriConsumer) arr[4])));
		UI_OBJECT_CONSTRUCTORS.put(DelegatingTextUIObject.class, listDelegatingTextUIObject);

		/*                 GradientQuadUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listGradientQuadUIObject = new ArrayList<>();
		listGradientQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, GradientQuadMesh.class}, (Object[] arr) -> (UIObject) new GradientQuadUIObject((String) arr[0], (GradientQuadMesh) arr[1])));
		listGradientQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, GradientQuadMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new GradientQuadUIObject((String) arr[0], (GradientQuadMesh) arr[1], (Transform3D) arr[2])));
		listGradientQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, GradientQuadMesh.class, GradientDirection.class}, (Object[] arr) -> (UIObject) new GradientQuadUIObject((String) arr[0], (GradientQuadMesh) arr[1], (GradientDirection) arr[2])));
		listGradientQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, GradientQuadMesh.class, Transform3D.class, GradientDirection.class}, (Object[] arr) -> (UIObject) new GradientQuadUIObject((String) arr[0], (GradientQuadMesh) arr[1], (Transform3D) arr[2], (GradientDirection) arr[3])));
		listGradientQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, GradientQuadMesh.class, GradientDirection.class, Vector4f.class}, (Object[] arr) -> (UIObject) new GradientQuadUIObject((String) arr[0], (GradientQuadMesh) arr[1], (GradientDirection) arr[2], (Vector4f) arr[3])));
		listGradientQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, GradientQuadMesh.class, Transform3D.class, GradientDirection.class, Vector4f.class}, (Object[] arr) -> (UIObject) new GradientQuadUIObject((String) arr[0], (GradientQuadMesh) arr[1], (Transform3D) arr[2], (GradientDirection) arr[3], (Vector4f) arr[4])));
		listGradientQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, GradientQuadMesh.class, GradientDirection.class, Vector2f.class, Vector4f.class}, (Object[] arr) -> (UIObject) new GradientQuadUIObject((String) arr[0], (GradientQuadMesh) arr[1], (GradientDirection) arr[2], (Vector2f) arr[3], (Vector4f) arr[4])));
		listGradientQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, GradientQuadMesh.class, GradientDirection.class, Vector4f.class, Vector4f.class}, (Object[] arr) -> (UIObject) new GradientQuadUIObject((String) arr[0], (GradientQuadMesh) arr[1], (GradientDirection) arr[2], (Vector4f) arr[3], (Vector4f) arr[4])));
		listGradientQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, GradientQuadMesh.class, Transform3D.class, GradientDirection.class, Vector2f.class, Vector4f.class}, (Object[] arr) -> (UIObject) new GradientQuadUIObject((String) arr[0], (GradientQuadMesh) arr[1], (Transform3D) arr[2], (GradientDirection) arr[3], (Vector2f) arr[4], (Vector4f) arr[5])));
		listGradientQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, GradientQuadMesh.class, Transform3D.class, GradientDirection.class, Vector4f.class, Vector4f.class}, (Object[] arr) -> (UIObject) new GradientQuadUIObject((String) arr[0], (GradientQuadMesh) arr[1], (Transform3D) arr[2], (GradientDirection) arr[3], (Vector4f) arr[4], (Vector4f) arr[5])));
		listGradientQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, GradientQuadMesh.class, GradientDirection.class, Vector2f.class, Vector4f.class, Vector4f.class}, (Object[] arr) -> (UIObject) new GradientQuadUIObject((String) arr[0], (GradientQuadMesh) arr[1], (GradientDirection) arr[2], (Vector2f) arr[3], (Vector4f) arr[4], (Vector4f) arr[5])));
		listGradientQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, GradientQuadMesh.class, Transform3D.class, GradientDirection.class, Vector2f.class, Vector4f.class, Vector4f.class}, (Object[] arr) -> (UIObject) new GradientQuadUIObject((String) arr[0], (GradientQuadMesh) arr[1], (Transform3D) arr[2], (GradientDirection) arr[3], (Vector2f) arr[4], (Vector4f) arr[5], (Vector4f) arr[6])));
		listGradientQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, GradientQuadMesh.class, GradientDirection.class, Vector2f.class, Vector4f.class, Vector4f.class, Vector4f.class}, (Object[] arr) -> (UIObject) new GradientQuadUIObject((String) arr[0], (GradientQuadMesh) arr[1], (GradientDirection) arr[2], (Vector2f) arr[3], (Vector4f) arr[4], (Vector4f) arr[5], (Vector4f) arr[6])));
		listGradientQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, GradientQuadMesh.class, Transform3D.class, GradientDirection.class, Vector2f.class, Vector4f.class, Vector4f.class, Vector4f.class}, (Object[] arr) -> (UIObject) new GradientQuadUIObject((String) arr[0], (GradientQuadMesh) arr[1], (Transform3D) arr[2], (GradientDirection) arr[3], (Vector2f) arr[4], (Vector4f) arr[5], (Vector4f) arr[6], (Vector4f) arr[7])));
		UI_OBJECT_CONSTRUCTORS.put(GradientQuadUIObject.class, listGradientQuadUIObject);

		/*                 LargeLogoUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listLargeLogoUIObject = new ArrayList<>();
		listLargeLogoUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new LargeLogoUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		listLargeLogoUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new LargeLogoUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(LargeLogoUIObject.class, listLargeLogoUIObject);

		/*                 SpacerUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listSpacerUIObject = new ArrayList<>();
		listSpacerUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class, Vector2f.class}, (Object[] arr) -> (UIObject) new SpacerUIObject((String) arr[0], (Mesh) arr[1], (Vector2f) arr[2])));
		listSpacerUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class, Transform3D.class, Vector2f.class}, (Object[] arr) -> (UIObject) new SpacerUIObject((String) arr[0], (Mesh) arr[1], (Transform3D) arr[2], (Vector2f) arr[3])));
		UI_OBJECT_CONSTRUCTORS.put(SpacerUIObject.class, listSpacerUIObject);

		/*                 OptionKeyUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listOptionKeyUIObject = new ArrayList<>();
		listOptionKeyUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, Scale2dDir.class}, (Object[] arr) -> (UIObject) new OptionKeyUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (Scale2dDir) arr[3])));
		listOptionKeyUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, Scale2dDir.class, Transform3D.class}, (Object[] arr) -> (UIObject) new OptionKeyUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (Scale2dDir) arr[3], (Transform3D) arr[4])));
		UI_OBJECT_CONSTRUCTORS.put(OptionKeyUIObject.class, listOptionKeyUIObject);

		/*                 LevelButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listLevelButtonUIObject = new ArrayList<>();
		listLevelButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, String.class}, (Object[] arr) -> (UIObject) new LevelButtonUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (String) arr[2])));
		listLevelButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, String.class, LevelState.class}, (Object[] arr) -> (UIObject) new LevelButtonUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (String) arr[2], (LevelState) arr[3])));
		listLevelButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class, String.class}, (Object[] arr) -> (UIObject) new LevelButtonUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2], (String) arr[3])));
		listLevelButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class, String.class, LevelState.class}, (Object[] arr) -> (UIObject) new LevelButtonUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2], (String) arr[3], (LevelState) arr[4])));
		UI_OBJECT_CONSTRUCTORS.put(LevelButtonUIObject.class, listLevelButtonUIObject);

		/*                 ScrollContainerUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listScrollContainerUIObjectGroup = new ArrayList<>();
		listScrollContainerUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Direction.class, float.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ScrollContainerUIObjectGroup((String) arr[0], (Direction) arr[1], (float) arr[2], (UIObject[]) arr[3])));
		listScrollContainerUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Direction.class, float.class, Layout.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ScrollContainerUIObjectGroup((String) arr[0], (Direction) arr[1], (float) arr[2], (Layout) arr[3], (UIObject[]) arr[4])));
		listScrollContainerUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Transform3D.class, Direction.class, float.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ScrollContainerUIObjectGroup((String) arr[0], (Transform3D) arr[1], (Direction) arr[2], (float) arr[3], (UIObject[]) arr[4])));
		listScrollContainerUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Vector3fc.class, Direction.class, float.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ScrollContainerUIObjectGroup((String) arr[0], (Vector3fc) arr[1], (Direction) arr[2], (float) arr[3], (UIObject[]) arr[4])));
		listScrollContainerUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, Direction.class, float.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ScrollContainerUIObjectGroup((String) arr[0], (UIObjectGroup) arr[1], (Direction) arr[2], (float) arr[3], (UIObject[]) arr[4])));
		listScrollContainerUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Transform3D.class, Direction.class, float.class, Layout.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ScrollContainerUIObjectGroup((String) arr[0], (Transform3D) arr[1], (Direction) arr[2], (float) arr[3], (Layout) arr[4], (UIObject[]) arr[5])));
		listScrollContainerUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Vector3fc.class, Direction.class, float.class, Layout.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ScrollContainerUIObjectGroup((String) arr[0], (Vector3fc) arr[1], (Direction) arr[2], (float) arr[3], (Layout) arr[4], (UIObject[]) arr[5])));
		listScrollContainerUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, Direction.class, float.class, Layout.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ScrollContainerUIObjectGroup((String) arr[0], (UIObjectGroup) arr[1], (Direction) arr[2], (float) arr[3], (Layout) arr[4], (UIObject[]) arr[5])));
		UI_OBJECT_CONSTRUCTORS.put(ScrollContainerUIObjectGroup.class, listScrollContainerUIObjectGroup);

		/*                 OffsetUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listOffsetUIObjectGroup = new ArrayList<>();
		listOffsetUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObject[].class}, (Object[] arr) -> (UIObject) new OffsetUIObjectGroup((String) arr[0], (UIObject[]) arr[1])));
		listOffsetUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new OffsetUIObjectGroup((String) arr[0], (Transform3D) arr[1], (UIObject[]) arr[2])));
		listOffsetUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, UIObject[].class}, (Object[] arr) -> (UIObject) new OffsetUIObjectGroup((String) arr[0], (UIObjectGroup) arr[1], (UIObject[]) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(OffsetUIObjectGroup.class, listOffsetUIObjectGroup);

		/*                 FlatQuadUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listFlatQuadUIObject = new ArrayList<>();
		listFlatQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new FlatQuadUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		listFlatQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Vector4f.class}, (Object[] arr) -> (UIObject) new FlatQuadUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Vector4f) arr[2])));
		listFlatQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new FlatQuadUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2])));
		listFlatQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class, Vector4f.class}, (Object[] arr) -> (UIObject) new FlatQuadUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2], (Vector4f) arr[3])));
		UI_OBJECT_CONSTRUCTORS.put(FlatQuadUIObject.class, listFlatQuadUIObject);

		/*                 BackButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listBackButtonUIObject = new ArrayList<>();
		listBackButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new BackButtonUIObject((String) arr[0], (TextEmitter) arr[1])));
		listBackButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new BackButtonUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(BackButtonUIObject.class, listBackButtonUIObject);

		/*                 ProgrammaticGrowOnHoverTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listProgrammaticGrowOnHoverTextUIObject = new ArrayList<>();
		listProgrammaticGrowOnHoverTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, Scale2dDir.class}, (Object[] arr) -> (UIObject) new ProgrammaticGrowOnHoverTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (Scale2dDir) arr[3])));
		listProgrammaticGrowOnHoverTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, Scale2dDir.class, Transform3D.class}, (Object[] arr) -> (UIObject) new ProgrammaticGrowOnHoverTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (Scale2dDir) arr[3], (Transform3D) arr[4])));
		UI_OBJECT_CONSTRUCTORS.put(ProgrammaticGrowOnHoverTextUIObject.class, listProgrammaticGrowOnHoverTextUIObject);

		/*                 LayoutOffsetUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listLayoutOffsetUIObjectGroup = new ArrayList<>();
		listLayoutOffsetUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObject[].class}, (Object[] arr) -> (UIObject) new LayoutOffsetUIObjectGroup((String) arr[0], (Layout) arr[1], (UIObject[]) arr[2])));
		listLayoutOffsetUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObjectGroup.class, UIObject[].class}, (Object[] arr) -> (UIObject) new LayoutOffsetUIObjectGroup((String) arr[0], (Layout) arr[1], (UIObjectGroup) arr[2], (UIObject[]) arr[3])));
		listLayoutOffsetUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new LayoutOffsetUIObjectGroup((String) arr[0], (Layout) arr[1], (Transform3D) arr[2], (UIObject[]) arr[3])));
		UI_OBJECT_CONSTRUCTORS.put(LayoutOffsetUIObjectGroup.class, listLayoutOffsetUIObjectGroup);

		/*                 ScrollDrivenUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listScrollDrivenUIObjectGroup = new ArrayList<>();
		listScrollDrivenUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Supplier.class, Direction.class, float.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ScrollDrivenUIObjectGroup((String) arr[0], (Supplier) arr[1], (Direction) arr[2], (float) arr[3], (UIObject[]) arr[4])));
		listScrollDrivenUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, Supplier.class, Direction.class, float.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ScrollDrivenUIObjectGroup((String) arr[0], (UIObjectGroup) arr[1], (Supplier) arr[2], (Direction) arr[3], (float) arr[4], (UIObject[]) arr[5])));
		listScrollDrivenUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Transform3D.class, Supplier.class, Direction.class, float.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ScrollDrivenUIObjectGroup((String) arr[0], (Transform3D) arr[1], (Supplier) arr[2], (Direction) arr[3], (float) arr[4], (UIObject[]) arr[5])));
		UI_OBJECT_CONSTRUCTORS.put(ScrollDrivenUIObjectGroup.class, listScrollDrivenUIObjectGroup);

		/*                 QuadUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listQuadUIObject = new ArrayList<>();
		listQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, QuadMesh.class}, (Object[] arr) -> (UIObject) new QuadUIObject((String) arr[0], (QuadMesh) arr[1])));
		listQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, QuadMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new QuadUIObject((String) arr[0], (QuadMesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(QuadUIObject.class, listQuadUIObject);

		/*                 TextureUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listTextureUIObject = new ArrayList<>();
		listTextureUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new TextureUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		listTextureUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new TextureUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(TextureUIObject.class, listTextureUIObject);

		/*                 VolumeTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listVolumeTextUIObject = new ArrayList<>();
		listVolumeTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new VolumeTextUIObject((String) arr[0], (TextEmitter) arr[1])));
		listVolumeTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new VolumeTextUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(VolumeTextUIObject.class, listVolumeTextUIObject);

		/*                 TextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listTextUIObject = new ArrayList<>();
		listTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new TextUIObject((String) arr[0], (TextEmitter) arr[1])));
		listTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new TextUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(TextUIObject.class, listTextUIObject);

		/*                 ScrollBarUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listScrollBarUIObject = new ArrayList<>();
		listScrollBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class, Vector4f.class, Direction.class, Vector2f.class, Vector2f.class}, (Object[] arr) -> (UIObject) new ScrollBarUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2], (Vector4f) arr[3], (Direction) arr[4], (Vector2f) arr[5], (Vector2f) arr[6])));
		listScrollBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class, ColorMaterial.class, Direction.class, Vector2f.class, Vector2f.class}, (Object[] arr) -> (UIObject) new ScrollBarUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2], (ColorMaterial) arr[3], (Direction) arr[4], (Vector2f) arr[5], (Vector2f) arr[6])));
		listScrollBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class, ColorMaterial.class, Direction.class, Vector2f.class, Vector2f.class, float.class}, (Object[] arr) -> (UIObject) new ScrollBarUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2], (ColorMaterial) arr[3], (Direction) arr[4], (Vector2f) arr[5], (Vector2f) arr[6], (float) arr[7])));
		listScrollBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class, Vector4f.class, Direction.class, Vector2f.class, Vector2f.class, float.class}, (Object[] arr) -> (UIObject) new ScrollBarUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2], (Vector4f) arr[3], (Direction) arr[4], (Vector2f) arr[5], (Vector2f) arr[6], (float) arr[7])));
		UI_OBJECT_CONSTRUCTORS.put(ScrollBarUIObject.class, listScrollBarUIObject);

		/*                 VolumeSliderUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listVolumeSliderUIObject = new ArrayList<>();
		listVolumeSliderUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new VolumeSliderUIObject((String) arr[0], (TextEmitter) arr[1])));
		listVolumeSliderUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new VolumeSliderUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		listVolumeSliderUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, float.class, float.class, float.class, int.class}, (Object[] arr) -> (UIObject) new VolumeSliderUIObject((String) arr[0], (TextEmitter) arr[1], (float) arr[2], (float) arr[3], (float) arr[4], (int) arr[5])));
		listVolumeSliderUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class, float.class, float.class, float.class, int.class}, (Object[] arr) -> (UIObject) new VolumeSliderUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2], (float) arr[3], (float) arr[4], (float) arr[5], (int) arr[6])));
		UI_OBJECT_CONSTRUCTORS.put(VolumeSliderUIObject.class, listVolumeSliderUIObject);

	}

	@SuppressWarnings("unchecked")
	public static <T extends UIObject> T create(final Class<T> clazz, final Object... args) {
		return (T) get(clazz, args).apply(args);
	}

	public static <T extends UIObject> InternalConstructorFunction<UIObject> get(final Class<T> clazz,
			final Object... args) {
		if (UI_OBJECT_CONSTRUCTORS.containsKey(clazz)) {
			final Optional<InternalConstructorFunction<UIObject>> bestConstructor = UI_OBJECT_CONSTRUCTORS.get(clazz).parallelStream().filter((v) -> v.matches(args)).findFirst();
			if (bestConstructor.isPresent()) {
				return bestConstructor.get();
			} else {
				throw new UIObjectConstructorNotFound(clazz, args);
			}
		} else {
			throw new UIObjectNotFound(clazz, args);
		}
	}
}
