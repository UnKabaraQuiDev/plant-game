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
import lu.kbra.plant_game.engine.entity.ui.btn.BackButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.MoneyUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.OptionsButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.PlayButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.QuitButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.DelegatingTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.GrowOnHoverTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.QuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.TextUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.TextureUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.CursorUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.GradientQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.texture.LargeLogoUIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadLoadedMesh;
import lu.kbra.plant_game.engine.render.GradientDirection;
import lu.kbra.plant_game.engine.render.GradientQuadMesh;
import lu.kbra.plant_game.engine.util.InternalConstructorFunction;
import lu.kbra.plant_game.engine.util.exceptions.UIObjectConstructorNotFound;
import lu.kbra.plant_game.engine.util.exceptions.UIObjectNotFound;
import lu.kbra.standalone.gameengine.geom.QuadMesh;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;
import lu.pcy113.pclib.impl.TriConsumer;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class UIObjectRegistry {
	private static final Map<Class<? extends UIObject>, List<InternalConstructorFunction<UIObject>>> UI_OBJECT_CONSTRUCTORS;

	static {
		UI_OBJECT_CONSTRUCTORS = new HashMap<>();

		/*                 QuadUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listQuadUIObject = new ArrayList<>();
		listQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, QuadMesh.class}, (Object[] arr) -> (UIObject) new QuadUIObject((String) arr[0], (QuadMesh) arr[1])));
		listQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, QuadMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new QuadUIObject((String) arr[0], (QuadMesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(QuadUIObject.class, listQuadUIObject);

		/*                 TextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listTextUIObject = new ArrayList<>();
		listTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new TextUIObject((String) arr[0], (TextEmitter) arr[1])));
		listTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new TextUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(TextUIObject.class, listTextUIObject);

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

		/*                 TextureUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listTextureUIObject = new ArrayList<>();
		listTextureUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadLoadedMesh.class}, (Object[] arr) -> (UIObject) new TextureUIObject((String) arr[0], (TexturedQuadLoadedMesh) arr[1])));
		listTextureUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadLoadedMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new TextureUIObject((String) arr[0], (TexturedQuadLoadedMesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(TextureUIObject.class, listTextureUIObject);

		/*                 DelegatingTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listDelegatingTextUIObject = new ArrayList<>();
		listDelegatingTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, TriConsumer.class, TriConsumer.class}, (Object[] arr) -> (UIObject) new DelegatingTextUIObject((String) arr[0], (TextEmitter) arr[1], (TriConsumer) arr[2], (TriConsumer) arr[3])));
		listDelegatingTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class, TriConsumer.class, TriConsumer.class}, (Object[] arr) -> (UIObject) new DelegatingTextUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2], (TriConsumer) arr[3], (TriConsumer) arr[4])));
		UI_OBJECT_CONSTRUCTORS.put(DelegatingTextUIObject.class, listDelegatingTextUIObject);

		/*                 GrowOnHoverTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listGrowOnHoverTextUIObject = new ArrayList<>();
		listGrowOnHoverTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new GrowOnHoverTextUIObject((String) arr[0], (TextEmitter) arr[1])));
		listGrowOnHoverTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new GrowOnHoverTextUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(GrowOnHoverTextUIObject.class, listGrowOnHoverTextUIObject);

		/*                 LargeLogoUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listLargeLogoUIObject = new ArrayList<>();
		listLargeLogoUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadLoadedMesh.class}, (Object[] arr) -> (UIObject) new LargeLogoUIObject((String) arr[0], (TexturedQuadLoadedMesh) arr[1])));
		listLargeLogoUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadLoadedMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new LargeLogoUIObject((String) arr[0], (TexturedQuadLoadedMesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(LargeLogoUIObject.class, listLargeLogoUIObject);

		/*                 CursorUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listCursorUIObject = new ArrayList<>();
		listCursorUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadLoadedMesh.class}, (Object[] arr) -> (UIObject) new CursorUIObject((String) arr[0], (TexturedQuadLoadedMesh) arr[1])));
		listCursorUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadLoadedMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new CursorUIObject((String) arr[0], (TexturedQuadLoadedMesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(CursorUIObject.class, listCursorUIObject);

		/*                 MoneyUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listMoneyUIObject = new ArrayList<>();
		listMoneyUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadLoadedMesh.class}, (Object[] arr) -> (UIObject) new MoneyUIObject((String) arr[0], (TexturedQuadLoadedMesh) arr[1])));
		listMoneyUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadLoadedMesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new MoneyUIObject((String) arr[0], (TexturedQuadLoadedMesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(MoneyUIObject.class, listMoneyUIObject);

		/*                 BackButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listBackButtonUIObject = new ArrayList<>();
		listBackButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new BackButtonUIObject((String) arr[0], (TextEmitter) arr[1])));
		listBackButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new BackButtonUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(BackButtonUIObject.class, listBackButtonUIObject);

		/*                 OptionsButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listOptionsButtonUIObject = new ArrayList<>();
		listOptionsButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new OptionsButtonUIObject((String) arr[0], (TextEmitter) arr[1])));
		listOptionsButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new OptionsButtonUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(OptionsButtonUIObject.class, listOptionsButtonUIObject);

		/*                 QuitButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listQuitButtonUIObject = new ArrayList<>();
		listQuitButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new QuitButtonUIObject((String) arr[0], (TextEmitter) arr[1])));
		listQuitButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new QuitButtonUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(QuitButtonUIObject.class, listQuitButtonUIObject);

		/*                 PlayButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listPlayButtonUIObject = new ArrayList<>();
		listPlayButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new PlayButtonUIObject((String) arr[0], (TextEmitter) arr[1])));
		listPlayButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new PlayButtonUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(PlayButtonUIObject.class, listPlayButtonUIObject);

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
