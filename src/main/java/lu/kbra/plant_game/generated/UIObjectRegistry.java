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
import lu.kbra.plant_game.engine.entity.ui.btn.MoneyUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.OptionsButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.PlayButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.QuitButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.DelegatingTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.TextUIObject;
import lu.kbra.plant_game.engine.entity.ui.impl.UIObject;
import lu.kbra.plant_game.engine.util.InternalConstructorFunction;
import lu.kbra.plant_game.engine.util.exceptions.UIObjectConstructorNotFound;
import lu.kbra.plant_game.engine.util.exceptions.UIObjectNotFound;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;
import lu.pcy113.pclib.impl.TriConsumer;

public class UIObjectRegistry {
	private static final Map<Class<? extends UIObject>, List<InternalConstructorFunction<UIObject>>> UI_OBJECT_CONSTRUCTORS;

	static {
		UI_OBJECT_CONSTRUCTORS = new HashMap<>();

		/*                 TextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listTextUIObject = new ArrayList<>();
		listTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new TextUIObject((String) arr[0], (TextEmitter) arr[1])));
		listTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class}, (Object[] arr) -> (UIObject) new TextUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(TextUIObject.class, listTextUIObject);

		/*                 MoneyUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listMoneyUIObject = new ArrayList<>();
		listMoneyUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (UIObject) new MoneyUIObject((String) arr[0], (Mesh) arr[1])));
		listMoneyUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class, Transform3D.class}, (Object[] arr) -> (UIObject) new MoneyUIObject((String) arr[0], (Mesh) arr[1], (Transform3D) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(MoneyUIObject.class, listMoneyUIObject);

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

		/*                 DelegatingTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listDelegatingTextUIObject = new ArrayList<>();
		listDelegatingTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, TriConsumer.class, TriConsumer.class}, (Object[] arr) -> (UIObject) new DelegatingTextUIObject((String) arr[0], (TextEmitter) arr[1], (TriConsumer) arr[2], (TriConsumer) arr[3])));
		listDelegatingTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class, Transform3D.class, TriConsumer.class, TriConsumer.class}, (Object[] arr) -> (UIObject) new DelegatingTextUIObject((String) arr[0], (TextEmitter) arr[1], (Transform3D) arr[2], (TriConsumer) arr[3], (TriConsumer) arr[4])));
		UI_OBJECT_CONSTRUCTORS.put(DelegatingTextUIObject.class, listDelegatingTextUIObject);

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
