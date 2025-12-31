// @formatter:off
package lu.kbra.plant_game.generated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.joml.Vector2f;
import org.joml.Vector4f;

import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.BackButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.LevelButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.OptionsButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.PlayButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.QuitButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.gradient.GradientQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.Scale2dDir;
import lu.kbra.plant_game.engine.entity.ui.prim.FlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.scroller.ScrollBarUIObject;
import lu.kbra.plant_game.engine.entity.ui.slider.SliderUIObject;
import lu.kbra.plant_game.engine.entity.ui.slider.VolumeSliderUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.IntegerTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.OptionKeyUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.PercentageIntTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.PercentageSignedIntTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticGrowOnHoverTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.SignedIntegerTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.VolumeTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.textinput.TextBoxUIObject;
import lu.kbra.plant_game.engine.entity.ui.textinput.TextFieldUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.CursorUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.EnergyIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.LargeLogoUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.LeafIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.MoneyIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.StarIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.WaterIconUIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.render.GradientDirection;
import lu.kbra.plant_game.engine.render.GradientQuadMesh;
import lu.kbra.plant_game.engine.scene.world.LevelState;
import lu.kbra.plant_game.engine.util.InternalConstructorFunction;
import lu.kbra.plant_game.engine.util.exceptions.UIObjectConstructorNotFound;
import lu.kbra.plant_game.engine.util.exceptions.UIObjectNotFound;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureWrap;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;

public class UIObjectRegistry {
	public static final Map<Class<? extends UIObject>, List<InternalConstructorFunction<UIObject>>> UI_OBJECT_CONSTRUCTORS;

	public static final Map<Class<? extends UIObject>, String> DATA_PATH;

	public static final Map<Class<? extends UIObject>, Integer> BUFFER_SIZE;

	public static final Map<Class<? extends UIObject>, TextureFilter> TEXTURE_FILTER;

	public static final Map<Class<? extends UIObject>, TextureWrap> TEXTURE_WRAP;

	static {
		UI_OBJECT_CONSTRUCTORS = new HashMap<>();
		DATA_PATH = new HashMap<>();
		BUFFER_SIZE = new HashMap<>();
		TEXTURE_FILTER = new HashMap<>();
		TEXTURE_WRAP = new HashMap<>();

		/*                 TextFieldUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listTextFieldUIObject = new ArrayList<>();
		listTextFieldUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new TextFieldUIObject((String) arr[0], (TextEmitter) arr[1])));
		listTextFieldUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new TextFieldUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(TextFieldUIObject.class, listTextFieldUIObject);
		BUFFER_SIZE.put(TextFieldUIObject.class, 25);
		DATA_PATH.put(TextFieldUIObject.class, "localization:string-placeholder");

		/*                 VolumeTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listVolumeTextUIObject = new ArrayList<>();
		listVolumeTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new VolumeTextUIObject((String) arr[0], (TextEmitter) arr[1])));
		listVolumeTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new VolumeTextUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(VolumeTextUIObject.class, listVolumeTextUIObject);
		DATA_PATH.put(VolumeTextUIObject.class, "localization:title.volume");

		/*                 SliderUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listSliderUIObject = new ArrayList<>();
		listSliderUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, float.class, float.class, float.class, int.class}, (Object[] arr) -> (UIObject) new SliderUIObject((String) arr[0], (TextEmitter) arr[1], (float) arr[2], (float) arr[3], (float) arr[4], (int) arr[5])));
		listSliderUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class, float.class, float.class, float.class, int.class}, (Object[] arr) -> (UIObject) new SliderUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2], (float) arr[3], (float) arr[4], (float) arr[5], (int) arr[6])));
		UI_OBJECT_CONSTRUCTORS.put(SliderUIObject.class, listSliderUIObject);
		DATA_PATH.put(SliderUIObject.class, "");

		/*                 ProgrammaticTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listProgrammaticTextUIObject = new ArrayList<>();
		listProgrammaticTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class}, (Object[] arr) -> (UIObject) new ProgrammaticTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2])));
		listProgrammaticTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, Transform3D.class}, (Object[] arr) -> (UIObject) new ProgrammaticTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (Transform3D) arr[3])));
		UI_OBJECT_CONSTRUCTORS.put(ProgrammaticTextUIObject.class, listProgrammaticTextUIObject);
		DATA_PATH.put(ProgrammaticTextUIObject.class, "localization:string-placeholder");

		/*                 FlatQuadUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listFlatQuadUIObject = new ArrayList<>();
		listFlatQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new FlatQuadUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		listFlatQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, ColorMaterial.class}, (Object[] arr) -> (UIObject) new FlatQuadUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (ColorMaterial) arr[2])));
		listFlatQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new FlatQuadUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2])));
		listFlatQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Vector4f.class}, (Object[] arr) -> (UIObject) new FlatQuadUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Vector4f) arr[2])));
		listFlatQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class, ColorMaterial.class}, (Object[] arr) -> (UIObject) new FlatQuadUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2], (ColorMaterial) arr[3])));
		listFlatQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class, Vector4f.class}, (Object[] arr) -> (UIObject) new FlatQuadUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2], (Vector4f) arr[3])));
		UI_OBJECT_CONSTRUCTORS.put(FlatQuadUIObject.class, listFlatQuadUIObject);
		DATA_PATH.put(FlatQuadUIObject.class, "");

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
		DATA_PATH.put(GradientQuadUIObject.class, "");

		/*                 BackButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listBackButtonUIObject = new ArrayList<>();
		listBackButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new BackButtonUIObject((String) arr[0], (TextEmitter) arr[1])));
		listBackButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new BackButtonUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(BackButtonUIObject.class, listBackButtonUIObject);
		DATA_PATH.put(BackButtonUIObject.class, "localization:btn.back");

		/*                 OptionsButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listOptionsButtonUIObject = new ArrayList<>();
		listOptionsButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new OptionsButtonUIObject((String) arr[0], (TextEmitter) arr[1])));
		listOptionsButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new OptionsButtonUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(OptionsButtonUIObject.class, listOptionsButtonUIObject);
		DATA_PATH.put(OptionsButtonUIObject.class, "localization:btn.options");

		/*                 QuitButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listQuitButtonUIObject = new ArrayList<>();
		listQuitButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new QuitButtonUIObject((String) arr[0], (TextEmitter) arr[1])));
		listQuitButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new QuitButtonUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(QuitButtonUIObject.class, listQuitButtonUIObject);
		DATA_PATH.put(QuitButtonUIObject.class, "localization:btn.quit");

		/*                 PlayButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listPlayButtonUIObject = new ArrayList<>();
		listPlayButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new PlayButtonUIObject((String) arr[0], (TextEmitter) arr[1])));
		listPlayButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new PlayButtonUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(PlayButtonUIObject.class, listPlayButtonUIObject);
		DATA_PATH.put(PlayButtonUIObject.class, "localization:btn.play");

		/*                 TextBoxUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listTextBoxUIObject = new ArrayList<>();
		listTextBoxUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new TextBoxUIObject((String) arr[0], (TextEmitter) arr[1])));
		listTextBoxUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new TextBoxUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(TextBoxUIObject.class, listTextBoxUIObject);
		BUFFER_SIZE.put(TextBoxUIObject.class, 25);
		DATA_PATH.put(TextBoxUIObject.class, "localization:string-placeholder");

		/*                 VolumeSliderUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listVolumeSliderUIObject = new ArrayList<>();
		listVolumeSliderUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new VolumeSliderUIObject((String) arr[0], (TextEmitter) arr[1])));
		listVolumeSliderUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new VolumeSliderUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		listVolumeSliderUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, float.class, float.class, float.class, int.class}, (Object[] arr) -> (UIObject) new VolumeSliderUIObject((String) arr[0], (TextEmitter) arr[1], (float) arr[2], (float) arr[3], (float) arr[4], (int) arr[5])));
		listVolumeSliderUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class, float.class, float.class, float.class, int.class}, (Object[] arr) -> (UIObject) new VolumeSliderUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2], (float) arr[3], (float) arr[4], (float) arr[5], (int) arr[6])));
		UI_OBJECT_CONSTRUCTORS.put(VolumeSliderUIObject.class, listVolumeSliderUIObject);
		DATA_PATH.put(VolumeSliderUIObject.class, "localization:slider.volume");

		/*                 IntegerTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listIntegerTextUIObject = new ArrayList<>();
		listIntegerTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class}, (Object[] arr) -> (UIObject) new IntegerTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2])));
		listIntegerTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, Transform3D.class}, (Object[] arr) -> (UIObject) new IntegerTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (Transform3D) arr[3])));
		listIntegerTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, ColorMaterial.class}, (Object[] arr) -> (UIObject) new IntegerTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (ColorMaterial) arr[3])));
		listIntegerTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, int.class}, (Object[] arr) -> (UIObject) new IntegerTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (int) arr[3])));
		listIntegerTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, int.class, boolean.class}, (Object[] arr) -> (UIObject) new IntegerTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (int) arr[3], (boolean) arr[4])));
		listIntegerTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, int.class, Transform3D.class}, (Object[] arr) -> (UIObject) new IntegerTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (int) arr[3], (Transform3D) arr[4])));
		listIntegerTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, ColorMaterial.class, Transform3D.class}, (Object[] arr) -> (UIObject) new IntegerTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (ColorMaterial) arr[3], (Transform3D) arr[4])));
		listIntegerTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, int.class, boolean.class, Transform3D.class}, (Object[] arr) -> (UIObject) new IntegerTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (int) arr[3], (boolean) arr[4], (Transform3D) arr[5])));
		listIntegerTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, int.class, boolean.class, boolean.class, int.class}, (Object[] arr) -> (UIObject) new IntegerTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (int) arr[3], (boolean) arr[4], (boolean) arr[5], (int) arr[6])));
		listIntegerTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, int.class, boolean.class, boolean.class, int.class, Transform3D.class}, (Object[] arr) -> (UIObject) new IntegerTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (int) arr[3], (boolean) arr[4], (boolean) arr[5], (int) arr[6], (Transform3D) arr[7])));
		listIntegerTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, int.class, boolean.class, boolean.class, boolean.class, int.class, ColorMaterial.class}, (Object[] arr) -> (UIObject) new IntegerTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (int) arr[3], (boolean) arr[4], (boolean) arr[5], (boolean) arr[6], (int) arr[7], (ColorMaterial) arr[8])));
		listIntegerTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, int.class, boolean.class, boolean.class, boolean.class, int.class, ColorMaterial.class, Transform3D.class}, (Object[] arr) -> (UIObject) new IntegerTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (int) arr[3], (boolean) arr[4], (boolean) arr[5], (boolean) arr[6], (int) arr[7], (ColorMaterial) arr[8], (Transform3D) arr[9])));
		UI_OBJECT_CONSTRUCTORS.put(IntegerTextUIObject.class, listIntegerTextUIObject);
		BUFFER_SIZE.put(IntegerTextUIObject.class, 10);
		DATA_PATH.put(IntegerTextUIObject.class, "localization:string-placeholder");

		/*                 ProgrammaticGrowOnHoverTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listProgrammaticGrowOnHoverTextUIObject = new ArrayList<>();
		listProgrammaticGrowOnHoverTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, Scale2dDir.class}, (Object[] arr) -> (UIObject) new ProgrammaticGrowOnHoverTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (Scale2dDir) arr[3])));
		listProgrammaticGrowOnHoverTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, Scale2dDir.class, Transform3D.class}, (Object[] arr) -> (UIObject) new ProgrammaticGrowOnHoverTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (Scale2dDir) arr[3], (Transform3D) arr[4])));
		UI_OBJECT_CONSTRUCTORS.put(ProgrammaticGrowOnHoverTextUIObject.class, listProgrammaticGrowOnHoverTextUIObject);
		DATA_PATH.put(ProgrammaticGrowOnHoverTextUIObject.class, "localization:string-placeholder");

		/*                 LeafIconUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listLeafIconUIObject = new ArrayList<>();
		listLeafIconUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new LeafIconUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		listLeafIconUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new LeafIconUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(LeafIconUIObject.class, listLeafIconUIObject);
		DATA_PATH.put(LeafIconUIObject.class, "image:classpath:/icons/star-128.png");

		/*                 StarIconUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listStarIconUIObject = new ArrayList<>();
		listStarIconUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new StarIconUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		listStarIconUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new StarIconUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(StarIconUIObject.class, listStarIconUIObject);
		DATA_PATH.put(StarIconUIObject.class, "image:classpath:/icons/star-128.png");

		/*                 EnergyIconUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listEnergyIconUIObject = new ArrayList<>();
		listEnergyIconUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new EnergyIconUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		listEnergyIconUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new EnergyIconUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(EnergyIconUIObject.class, listEnergyIconUIObject);
		DATA_PATH.put(EnergyIconUIObject.class, "image:classpath:/icons/energy-128.png");

		/*                 LargeLogoUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listLargeLogoUIObject = new ArrayList<>();
		listLargeLogoUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new LargeLogoUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		listLargeLogoUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new LargeLogoUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(LargeLogoUIObject.class, listLargeLogoUIObject);
		DATA_PATH.put(LargeLogoUIObject.class, "image:classpath:/icons/large_logo-256.png");
		TEXTURE_FILTER.put(LargeLogoUIObject.class, TextureFilter.LINEAR);
		TEXTURE_WRAP.put(LargeLogoUIObject.class, TextureWrap.REPEAT);

		/*                 LevelButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listLevelButtonUIObject = new ArrayList<>();
		listLevelButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, String.class}, (Object[] arr) -> (UIObject) new LevelButtonUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (String) arr[2])));
		listLevelButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, String.class, LevelState.class}, (Object[] arr) -> (UIObject) new LevelButtonUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (String) arr[2], (LevelState) arr[3])));
		listLevelButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class, String.class}, (Object[] arr) -> (UIObject) new LevelButtonUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2], (String) arr[3])));
		listLevelButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class, String.class, LevelState.class}, (Object[] arr) -> (UIObject) new LevelButtonUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2], (String) arr[3], (LevelState) arr[4])));
		UI_OBJECT_CONSTRUCTORS.put(LevelButtonUIObject.class, listLevelButtonUIObject);
		DATA_PATH.put(LevelButtonUIObject.class, "image:classpath:/icons/star-32.png");
		TEXTURE_FILTER.put(LevelButtonUIObject.class, TextureFilter.NEAREST);
		TEXTURE_WRAP.put(LevelButtonUIObject.class, TextureWrap.REPEAT);

		/*                 WaterIconUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listWaterIconUIObject = new ArrayList<>();
		listWaterIconUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new WaterIconUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		listWaterIconUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new WaterIconUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(WaterIconUIObject.class, listWaterIconUIObject);
		DATA_PATH.put(WaterIconUIObject.class, "image:classpath:/icons/water-128.png");

		/*                 CursorUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listCursorUIObject = new ArrayList<>();
		listCursorUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new CursorUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		listCursorUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new CursorUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(CursorUIObject.class, listCursorUIObject);
		DATA_PATH.put(CursorUIObject.class, "image:classpath:/icons/cursor-128.png");

		/*                 MoneyIconUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listMoneyIconUIObject = new ArrayList<>();
		listMoneyIconUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new MoneyIconUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		listMoneyIconUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new MoneyIconUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(MoneyIconUIObject.class, listMoneyIconUIObject);
		DATA_PATH.put(MoneyIconUIObject.class, "image:classpath:/icons/money-128.png");

		/*                 ScrollBarUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listScrollBarUIObject = new ArrayList<>();
		listScrollBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class, Vector4f.class, Direction.class, Vector2f.class, Vector2f.class}, (Object[] arr) -> (UIObject) new ScrollBarUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2], (Vector4f) arr[3], (Direction) arr[4], (Vector2f) arr[5], (Vector2f) arr[6])));
		listScrollBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class, ColorMaterial.class, Direction.class, Vector2f.class, Vector2f.class}, (Object[] arr) -> (UIObject) new ScrollBarUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2], (ColorMaterial) arr[3], (Direction) arr[4], (Vector2f) arr[5], (Vector2f) arr[6])));
		listScrollBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class, ColorMaterial.class, Direction.class, Vector2f.class, Vector2f.class, float.class}, (Object[] arr) -> (UIObject) new ScrollBarUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2], (ColorMaterial) arr[3], (Direction) arr[4], (Vector2f) arr[5], (Vector2f) arr[6], (float) arr[7])));
		listScrollBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class, Transform3D.class, Vector4f.class, Direction.class, Vector2f.class, Vector2f.class, float.class}, (Object[] arr) -> (UIObject) new ScrollBarUIObject((String) arr[0], (TexturedQuadMesh) arr[1], (Transform3D) arr[2], (Vector4f) arr[3], (Direction) arr[4], (Vector2f) arr[5], (Vector2f) arr[6], (float) arr[7])));
		UI_OBJECT_CONSTRUCTORS.put(ScrollBarUIObject.class, listScrollBarUIObject);
		DATA_PATH.put(ScrollBarUIObject.class, "");

		/*                 PercentageIntTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listPercentageIntTextUIObject = new ArrayList<>();
		listPercentageIntTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class}, (Object[] arr) -> (UIObject) new PercentageIntTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2])));
		listPercentageIntTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, int.class}, (Object[] arr) -> (UIObject) new PercentageIntTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (int) arr[3])));
		listPercentageIntTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, Transform3D.class}, (Object[] arr) -> (UIObject) new PercentageIntTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (Transform3D) arr[3])));
		listPercentageIntTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, ColorMaterial.class}, (Object[] arr) -> (UIObject) new PercentageIntTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (ColorMaterial) arr[3])));
		listPercentageIntTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, int.class, boolean.class}, (Object[] arr) -> (UIObject) new PercentageIntTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (int) arr[3], (boolean) arr[4])));
		listPercentageIntTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, int.class, Transform3D.class}, (Object[] arr) -> (UIObject) new PercentageIntTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (int) arr[3], (Transform3D) arr[4])));
		listPercentageIntTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, ColorMaterial.class, Transform3D.class}, (Object[] arr) -> (UIObject) new PercentageIntTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (ColorMaterial) arr[3], (Transform3D) arr[4])));
		listPercentageIntTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, int.class, boolean.class, Transform3D.class}, (Object[] arr) -> (UIObject) new PercentageIntTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (int) arr[3], (boolean) arr[4], (Transform3D) arr[5])));
		listPercentageIntTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, int.class, boolean.class, boolean.class, int.class}, (Object[] arr) -> (UIObject) new PercentageIntTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (int) arr[3], (boolean) arr[4], (boolean) arr[5], (int) arr[6])));
		listPercentageIntTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, int.class, boolean.class, boolean.class, int.class, Transform3D.class}, (Object[] arr) -> (UIObject) new PercentageIntTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (int) arr[3], (boolean) arr[4], (boolean) arr[5], (int) arr[6], (Transform3D) arr[7])));
		listPercentageIntTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, int.class, boolean.class, boolean.class, boolean.class, int.class, ColorMaterial.class}, (Object[] arr) -> (UIObject) new PercentageIntTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (int) arr[3], (boolean) arr[4], (boolean) arr[5], (boolean) arr[6], (int) arr[7], (ColorMaterial) arr[8])));
		listPercentageIntTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, int.class, boolean.class, boolean.class, boolean.class, int.class, ColorMaterial.class, Transform3D.class}, (Object[] arr) -> (UIObject) new PercentageIntTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (int) arr[3], (boolean) arr[4], (boolean) arr[5], (boolean) arr[6], (int) arr[7], (ColorMaterial) arr[8], (Transform3D) arr[9])));
		UI_OBJECT_CONSTRUCTORS.put(PercentageIntTextUIObject.class, listPercentageIntTextUIObject);
		DATA_PATH.put(PercentageIntTextUIObject.class, "");

		/*                 SignedIntegerTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listSignedIntegerTextUIObject = new ArrayList<>();
		listSignedIntegerTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, ColorMaterial.class, ColorMaterial.class, ColorMaterial.class}, (Object[] arr) -> (UIObject) new SignedIntegerTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (ColorMaterial) arr[3], (ColorMaterial) arr[4], (ColorMaterial) arr[5])));
		listSignedIntegerTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, ColorMaterial.class, ColorMaterial.class, ColorMaterial.class, Transform3D.class}, (Object[] arr) -> (UIObject) new SignedIntegerTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (ColorMaterial) arr[3], (ColorMaterial) arr[4], (ColorMaterial) arr[5], (Transform3D) arr[6])));
		UI_OBJECT_CONSTRUCTORS.put(SignedIntegerTextUIObject.class, listSignedIntegerTextUIObject);
		BUFFER_SIZE.put(SignedIntegerTextUIObject.class, 10);
		DATA_PATH.put(SignedIntegerTextUIObject.class, "localization:string-placeholder");

		/*                 OptionKeyUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listOptionKeyUIObject = new ArrayList<>();
		listOptionKeyUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, Scale2dDir.class}, (Object[] arr) -> (UIObject) new OptionKeyUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (Scale2dDir) arr[3])));
		listOptionKeyUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, Scale2dDir.class, Transform3D.class}, (Object[] arr) -> (UIObject) new OptionKeyUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (Scale2dDir) arr[3], (Transform3D) arr[4])));
		UI_OBJECT_CONSTRUCTORS.put(OptionKeyUIObject.class, listOptionKeyUIObject);
		DATA_PATH.put(OptionKeyUIObject.class, "");

		/*                 PercentageSignedIntTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listPercentageSignedIntTextUIObject = new ArrayList<>();
		listPercentageSignedIntTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, ColorMaterial.class, ColorMaterial.class, ColorMaterial.class}, (Object[] arr) -> (UIObject) new PercentageSignedIntTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (ColorMaterial) arr[3], (ColorMaterial) arr[4], (ColorMaterial) arr[5])));
		listPercentageSignedIntTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, String.class, ColorMaterial.class, ColorMaterial.class, ColorMaterial.class, Transform3D.class}, (Object[] arr) -> (UIObject) new PercentageSignedIntTextUIObject((String) arr[0], (TextEmitter) arr[1], (String) arr[2], (ColorMaterial) arr[3], (ColorMaterial) arr[4], (ColorMaterial) arr[5], (Transform3D) arr[6])));
		UI_OBJECT_CONSTRUCTORS.put(PercentageSignedIntTextUIObject.class, listPercentageSignedIntTextUIObject);
		DATA_PATH.put(PercentageSignedIntTextUIObject.class, "");

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
