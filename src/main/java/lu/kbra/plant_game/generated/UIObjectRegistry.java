// @formatter:off
package lu.kbra.plant_game.generated;

import java.lang.Class;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import lu.kbra.plant_game.engine.entity.ui.FlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.MeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.ProgrammaticTexturedQuadMeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.QuadMeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.TexturedQuadMeshUIObject;
import lu.kbra.plant_game.engine.entity.ui.UIObject;
import lu.kbra.plant_game.engine.entity.ui.bar.AnchoredProgressBarUIObject;
import lu.kbra.plant_game.engine.entity.ui.bar.ProgressBarUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.BackButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.LevelButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.OptionsButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.PlayButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.btn.QuitButtonUIObject;
import lu.kbra.plant_game.engine.entity.ui.data.Direction2d;
import lu.kbra.plant_game.engine.entity.ui.gradient.AnchoredGradientQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.gradient.ExtAnchoredGradientQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.gradient.GradientQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.group.BuildingTabListUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutOffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.LayoutScrollDrivenUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.OffsetUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.ScrollContainerUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.ScrollDrivenUIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.group.UIObjectGroup;
import lu.kbra.plant_game.engine.entity.ui.icon.CursorUIObject;
import lu.kbra.plant_game.engine.entity.ui.icon.EnergyIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.icon.LargeLogoUIObject;
import lu.kbra.plant_game.engine.entity.ui.icon.LeafIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.icon.MoneyIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.icon.StarIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.icon.WaterIconUIObject;
import lu.kbra.plant_game.engine.entity.ui.layout.SpacerUIObject;
import lu.kbra.plant_game.engine.entity.ui.prim.AnchoredFlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.prim.BuildingItemUIObject;
import lu.kbra.plant_game.engine.entity.ui.prim.ExtAnchoredFlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.prim.IBAnchoredFlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.prim.IndexedFlatQuadUIObject;
import lu.kbra.plant_game.engine.entity.ui.scroller.ScrollBarUIObject;
import lu.kbra.plant_game.engine.entity.ui.slider.AnchoredSliderUIObject;
import lu.kbra.plant_game.engine.entity.ui.slider.SliderUIObject;
import lu.kbra.plant_game.engine.entity.ui.slider.VolumeSliderUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.AnchoredProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.AnchoredTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.ExtAnchoredProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.GrowOnHoverTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.IntegerTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.OptionKeyUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.PercentageIntTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.PercentageSignedIntTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticGrowOnHoverTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.ProgrammaticTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.SignedIntegerTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.TextUIObject;
import lu.kbra.plant_game.engine.entity.ui.text.VolumeTextUIObject;
import lu.kbra.plant_game.engine.entity.ui.textinput.TextBoxUIObject;
import lu.kbra.plant_game.engine.entity.ui.textinput.TextFieldUIObject;
import lu.kbra.plant_game.engine.mesh.TexturedQuadMesh;
import lu.kbra.plant_game.engine.scene.ui.UIScene;
import lu.kbra.plant_game.engine.scene.ui.layout.Anchor;
import lu.kbra.plant_game.engine.scene.ui.layout.Layout;
import lu.kbra.plant_game.engine.util.InternalConstructorFunction;
import lu.kbra.plant_game.engine.util.exceptions.UIObjectConstructorNotFound;
import lu.kbra.plant_game.engine.util.exceptions.UIObjectNotFound;
import lu.kbra.plant_game.engine.window.input.KeyOption;
import lu.kbra.plant_game.vanilla.scene.menu.main.OptionKeyUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.menu.main.OptionVolumeUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.menu.main.OptionsUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.overlay.group.building.BuildingInfoUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.overlay.group.building.BuildingPanelUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.overlay.group.building.BuildingTabButtonUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.overlay.group.building.BuildingTabUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.overlay.group.impl.AnchoredFixedPBUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.overlay.group.impl.AnchoredLayoutUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.overlay.group.impl.AnchoredPBUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.overlay.group.impl.AnchoredParentUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.overlay.group.impl.BoundedUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.overlay.group.impl.FixedBoundsUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.overlay.group.impl.FixedPBUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.overlay.group.impl.MarginAnchoredUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.overlay.group.impl.PaddingParentUIObject;
import lu.kbra.plant_game.vanilla.scene.overlay.group.impl.ParentUIObjectGroup;
import lu.kbra.plant_game.vanilla.scene.overlay.stat_line.integer.AnchoredFixedIntegerStatLine;
import lu.kbra.plant_game.vanilla.scene.overlay.stat_line.integer.ExtAnchoredFixedIntegerStatLine;
import lu.kbra.plant_game.vanilla.scene.overlay.stat_line.integer.ExtAnchoredIntegerStatLine;
import lu.kbra.plant_game.vanilla.scene.overlay.stat_line.integer.FixedIntegerStatLine;
import lu.kbra.plant_game.vanilla.scene.overlay.stat_line.integer.IntegerStatLine;
import lu.kbra.standalone.gameengine.geom.Mesh;
import lu.kbra.standalone.gameengine.geom.QuadMesh;
import lu.kbra.standalone.gameengine.objs.text.TextEmitter;
import lu.kbra.standalone.gameengine.utils.consts.Direction;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureFilter;
import lu.kbra.standalone.gameengine.utils.gl.consts.TextureWrap;
import lu.kbra.standalone.gameengine.utils.transform.Transform3D;
import org.joml.Vector3fc;

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

		/*                 UIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listUIObjectGroup = new ArrayList<>();
		listUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObject[].class}, (Object[] arr) -> (UIObject) new UIObjectGroup((String) arr[0], (UIObject[]) arr[1])));
		listUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, List.class}, (Object[] arr) -> (UIObject) new UIObjectGroup((String) arr[0], (List) arr[1])));
		listUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, UIObject[].class}, (Object[] arr) -> (UIObject) new UIObjectGroup((String) arr[0], (UIObjectGroup) arr[1], (UIObject[]) arr[2])));
		listUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, UIScene.class, UIObject[].class}, (Object[] arr) -> (UIObject) new UIObjectGroup((String) arr[0], (UIScene) arr[1], (UIObject[]) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(UIObjectGroup.class, listUIObjectGroup);

		/*                 TextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listTextUIObject = new ArrayList<>();
		listTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new TextUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(TextUIObject.class, listTextUIObject);

		/*                 QuadMeshUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listQuadMeshUIObject = new ArrayList<>();
		listQuadMeshUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, QuadMesh.class}, (Object[] arr) -> (UIObject) new QuadMeshUIObject((String) arr[0], (QuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(QuadMeshUIObject.class, listQuadMeshUIObject);

		/*                 SpacerUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listSpacerUIObject = new ArrayList<>();
		listSpacerUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class}, (Object[] arr) -> (UIObject) new SpacerUIObject((String) arr[0])));
		UI_OBJECT_CONSTRUCTORS.put(SpacerUIObject.class, listSpacerUIObject);
		DATA_PATH.put(SpacerUIObject.class, "");

		/*                 MeshUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listMeshUIObject = new ArrayList<>();
		listMeshUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Mesh.class}, (Object[] arr) -> (UIObject) new MeshUIObject((String) arr[0], (Mesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(MeshUIObject.class, listMeshUIObject);

		/*                 TexturedQuadMeshUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listTexturedQuadMeshUIObject = new ArrayList<>();
		listTexturedQuadMeshUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new TexturedQuadMeshUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(TexturedQuadMeshUIObject.class, listTexturedQuadMeshUIObject);

		/*                 OffsetUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listOffsetUIObjectGroup = new ArrayList<>();
		listOffsetUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObject[].class}, (Object[] arr) -> (UIObject) new OffsetUIObjectGroup((String) arr[0], (UIObject[]) arr[1])));
		listOffsetUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new OffsetUIObjectGroup((String) arr[0], (Transform3D) arr[1], (UIObject[]) arr[2])));
		listOffsetUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, UIObject[].class}, (Object[] arr) -> (UIObject) new OffsetUIObjectGroup((String) arr[0], (UIObjectGroup) arr[1], (UIObject[]) arr[2])));
		listOffsetUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, UIScene.class, UIObject[].class}, (Object[] arr) -> (UIObject) new OffsetUIObjectGroup((String) arr[0], (UIScene) arr[1], (UIObject[]) arr[2])));
		listOffsetUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, UIScene.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new OffsetUIObjectGroup((String) arr[0], (UIScene) arr[1], (Transform3D) arr[2], (UIObject[]) arr[3])));
		listOffsetUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new OffsetUIObjectGroup((String) arr[0], (UIObjectGroup) arr[1], (Transform3D) arr[2], (UIObject[]) arr[3])));
		UI_OBJECT_CONSTRUCTORS.put(OffsetUIObjectGroup.class, listOffsetUIObjectGroup);

		/*                 GrowOnHoverTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listGrowOnHoverTextUIObject = new ArrayList<>();
		listGrowOnHoverTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new GrowOnHoverTextUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(GrowOnHoverTextUIObject.class, listGrowOnHoverTextUIObject);

		/*                 AnchoredTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listAnchoredTextUIObject = new ArrayList<>();
		listAnchoredTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new AnchoredTextUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(AnchoredTextUIObject.class, listAnchoredTextUIObject);

		/*                 TextFieldUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listTextFieldUIObject = new ArrayList<>();
		listTextFieldUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new TextFieldUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(TextFieldUIObject.class, listTextFieldUIObject);
		BUFFER_SIZE.put(TextFieldUIObject.class, 25);
		DATA_PATH.put(TextFieldUIObject.class, "localization:string-placeholder");

		/*                 SliderUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listSliderUIObject = new ArrayList<>();
		listSliderUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new SliderUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(SliderUIObject.class, listSliderUIObject);

		/*                 ProgrammaticTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listProgrammaticTextUIObject = new ArrayList<>();
		listProgrammaticTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new ProgrammaticTextUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(ProgrammaticTextUIObject.class, listProgrammaticTextUIObject);
		DATA_PATH.put(ProgrammaticTextUIObject.class, "localization:string-placeholder");

		/*                 GradientQuadUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listGradientQuadUIObject = new ArrayList<>();
		listGradientQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, QuadMesh.class}, (Object[] arr) -> (UIObject) new GradientQuadUIObject((String) arr[0], (QuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(GradientQuadUIObject.class, listGradientQuadUIObject);

		/*                 LeafIconUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listLeafIconUIObject = new ArrayList<>();
		listLeafIconUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new LeafIconUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(LeafIconUIObject.class, listLeafIconUIObject);
		DATA_PATH.put(LeafIconUIObject.class, "image:classpath:/icons/star-128.png");

		/*                 FlatQuadUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listFlatQuadUIObject = new ArrayList<>();
		listFlatQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new FlatQuadUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(FlatQuadUIObject.class, listFlatQuadUIObject);

		/*                 CursorUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listCursorUIObject = new ArrayList<>();
		listCursorUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new CursorUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(CursorUIObject.class, listCursorUIObject);
		DATA_PATH.put(CursorUIObject.class, "image:classpath:/icons/cursor-128.png");

		/*                 StarIconUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listStarIconUIObject = new ArrayList<>();
		listStarIconUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new StarIconUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(StarIconUIObject.class, listStarIconUIObject);
		DATA_PATH.put(StarIconUIObject.class, "image:classpath:/icons/star-128.png");

		/*                 EnergyIconUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listEnergyIconUIObject = new ArrayList<>();
		listEnergyIconUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new EnergyIconUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(EnergyIconUIObject.class, listEnergyIconUIObject);
		DATA_PATH.put(EnergyIconUIObject.class, "image:classpath:/icons/energy-128.png");

		/*                 MoneyIconUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listMoneyIconUIObject = new ArrayList<>();
		listMoneyIconUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new MoneyIconUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(MoneyIconUIObject.class, listMoneyIconUIObject);
		DATA_PATH.put(MoneyIconUIObject.class, "image:classpath:/icons/money-128.png");

		/*                 ProgrammaticTexturedQuadMeshUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listProgrammaticTexturedQuadMeshUIObject = new ArrayList<>();
		listProgrammaticTexturedQuadMeshUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new ProgrammaticTexturedQuadMeshUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(ProgrammaticTexturedQuadMeshUIObject.class, listProgrammaticTexturedQuadMeshUIObject);

		/*                 WaterIconUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listWaterIconUIObject = new ArrayList<>();
		listWaterIconUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new WaterIconUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(WaterIconUIObject.class, listWaterIconUIObject);
		DATA_PATH.put(WaterIconUIObject.class, "image:classpath:/icons/water-128.png");

		/*                 LargeLogoUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listLargeLogoUIObject = new ArrayList<>();
		listLargeLogoUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new LargeLogoUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(LargeLogoUIObject.class, listLargeLogoUIObject);
		DATA_PATH.put(LargeLogoUIObject.class, "image:classpath:/icons/large_logo-256.png");
		TEXTURE_FILTER.put(LargeLogoUIObject.class, TextureFilter.LINEAR);
		TEXTURE_WRAP.put(LargeLogoUIObject.class, TextureWrap.REPEAT);

		/*                 LevelButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listLevelButtonUIObject = new ArrayList<>();
		listLevelButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new LevelButtonUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(LevelButtonUIObject.class, listLevelButtonUIObject);
		DATA_PATH.put(LevelButtonUIObject.class, "image:classpath:/icons/star-32.png");
		TEXTURE_FILTER.put(LevelButtonUIObject.class, TextureFilter.NEAREST);
		TEXTURE_WRAP.put(LevelButtonUIObject.class, TextureWrap.REPEAT);

		/*                 BuildingTabButtonUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listBuildingTabButtonUIObjectGroup = new ArrayList<>();
		listBuildingTabButtonUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {Transform3D.class, BuildingTabUIObjectGroup.class}, (Object[] arr) -> (UIObject) new BuildingTabButtonUIObjectGroup((Transform3D) arr[0], (BuildingTabUIObjectGroup) arr[1])));
		listBuildingTabButtonUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Transform3D.class, String.class, ColorMaterial.class}, (Object[] arr) -> (UIObject) new BuildingTabButtonUIObjectGroup((String) arr[0], (Transform3D) arr[1], (String) arr[2], (ColorMaterial) arr[3])));
		UI_OBJECT_CONSTRUCTORS.put(BuildingTabButtonUIObjectGroup.class, listBuildingTabButtonUIObjectGroup);

		/*                 ScrollDrivenUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listScrollDrivenUIObjectGroup = new ArrayList<>();
		listScrollDrivenUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Supplier.class, Direction.class, float.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ScrollDrivenUIObjectGroup((String) arr[0], (Supplier) arr[1], (Direction) arr[2], (float) arr[3], (UIObject[]) arr[4])));
		listScrollDrivenUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, Supplier.class, Direction.class, float.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ScrollDrivenUIObjectGroup((String) arr[0], (UIObjectGroup) arr[1], (Supplier) arr[2], (Direction) arr[3], (float) arr[4], (UIObject[]) arr[5])));
		listScrollDrivenUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Transform3D.class, Supplier.class, Direction.class, float.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ScrollDrivenUIObjectGroup((String) arr[0], (Transform3D) arr[1], (Supplier) arr[2], (Direction) arr[3], (float) arr[4], (UIObject[]) arr[5])));
		UI_OBJECT_CONSTRUCTORS.put(ScrollDrivenUIObjectGroup.class, listScrollDrivenUIObjectGroup);

		/*                 LayoutOffsetUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listLayoutOffsetUIObjectGroup = new ArrayList<>();
		listLayoutOffsetUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObject[].class}, (Object[] arr) -> (UIObject) new LayoutOffsetUIObjectGroup((String) arr[0], (Layout) arr[1], (UIObject[]) arr[2])));
		listLayoutOffsetUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObjectGroup.class, UIObject[].class}, (Object[] arr) -> (UIObject) new LayoutOffsetUIObjectGroup((String) arr[0], (Layout) arr[1], (UIObjectGroup) arr[2], (UIObject[]) arr[3])));
		listLayoutOffsetUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new LayoutOffsetUIObjectGroup((String) arr[0], (Layout) arr[1], (Transform3D) arr[2], (UIObject[]) arr[3])));
		UI_OBJECT_CONSTRUCTORS.put(LayoutOffsetUIObjectGroup.class, listLayoutOffsetUIObjectGroup);

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

		/*                 ProgressBarUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listProgressBarUIObject = new ArrayList<>();
		listProgressBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ProgressBarUIObject((String) arr[0], (UIObject[]) arr[1])));
		listProgressBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ProgressBarUIObject((String) arr[0], (UIObjectGroup) arr[1], (UIObject[]) arr[2])));
		listProgressBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, UIScene.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ProgressBarUIObject((String) arr[0], (UIScene) arr[1], (UIObject[]) arr[2])));
		listProgressBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ProgressBarUIObject((String) arr[0], (Transform3D) arr[1], (UIObject[]) arr[2])));
		listProgressBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, UIScene.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ProgressBarUIObject((String) arr[0], (UIScene) arr[1], (Transform3D) arr[2], (UIObject[]) arr[3])));
		listProgressBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ProgressBarUIObject((String) arr[0], (UIObjectGroup) arr[1], (Transform3D) arr[2], (UIObject[]) arr[3])));
		listProgressBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, UIScene.class, Transform3D.class, float.class, float.class}, (Object[] arr) -> (UIObject) new ProgressBarUIObject((String) arr[0], (UIScene) arr[1], (Transform3D) arr[2], (float) arr[3], (float) arr[4])));
		UI_OBJECT_CONSTRUCTORS.put(ProgressBarUIObject.class, listProgressBarUIObject);

		/*                 BackButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listBackButtonUIObject = new ArrayList<>();
		listBackButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new BackButtonUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(BackButtonUIObject.class, listBackButtonUIObject);
		DATA_PATH.put(BackButtonUIObject.class, "localization:btn.back");

		/*                 OptionsButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listOptionsButtonUIObject = new ArrayList<>();
		listOptionsButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new OptionsButtonUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(OptionsButtonUIObject.class, listOptionsButtonUIObject);
		DATA_PATH.put(OptionsButtonUIObject.class, "localization:btn.options");

		/*                 QuitButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listQuitButtonUIObject = new ArrayList<>();
		listQuitButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new QuitButtonUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(QuitButtonUIObject.class, listQuitButtonUIObject);
		DATA_PATH.put(QuitButtonUIObject.class, "localization:btn.quit");

		/*                 PlayButtonUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listPlayButtonUIObject = new ArrayList<>();
		listPlayButtonUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new PlayButtonUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(PlayButtonUIObject.class, listPlayButtonUIObject);
		DATA_PATH.put(PlayButtonUIObject.class, "localization:btn.play");

		/*                 VolumeTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listVolumeTextUIObject = new ArrayList<>();
		listVolumeTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new VolumeTextUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(VolumeTextUIObject.class, listVolumeTextUIObject);
		DATA_PATH.put(VolumeTextUIObject.class, "localization:title.volume");

		/*                 TextBoxUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listTextBoxUIObject = new ArrayList<>();
		listTextBoxUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new TextBoxUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(TextBoxUIObject.class, listTextBoxUIObject);
		BUFFER_SIZE.put(TextBoxUIObject.class, 25);
		DATA_PATH.put(TextBoxUIObject.class, "localization:string-placeholder");

		/*                 AnchoredSliderUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listAnchoredSliderUIObject = new ArrayList<>();
		listAnchoredSliderUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new AnchoredSliderUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(AnchoredSliderUIObject.class, listAnchoredSliderUIObject);

		/*                 IntegerTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listIntegerTextUIObject = new ArrayList<>();
		listIntegerTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new IntegerTextUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(IntegerTextUIObject.class, listIntegerTextUIObject);
		BUFFER_SIZE.put(IntegerTextUIObject.class, 10);
		DATA_PATH.put(IntegerTextUIObject.class, "localization:string-placeholder");

		/*                 ProgrammaticGrowOnHoverTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listProgrammaticGrowOnHoverTextUIObject = new ArrayList<>();
		listProgrammaticGrowOnHoverTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new ProgrammaticGrowOnHoverTextUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(ProgrammaticGrowOnHoverTextUIObject.class, listProgrammaticGrowOnHoverTextUIObject);
		DATA_PATH.put(ProgrammaticGrowOnHoverTextUIObject.class, "localization:string-placeholder");

		/*                 AnchoredProgrammaticTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listAnchoredProgrammaticTextUIObject = new ArrayList<>();
		listAnchoredProgrammaticTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new AnchoredProgrammaticTextUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(AnchoredProgrammaticTextUIObject.class, listAnchoredProgrammaticTextUIObject);
		DATA_PATH.put(AnchoredProgrammaticTextUIObject.class, "localization:string-placeholder");

		/*                 AnchoredGradientQuadUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listAnchoredGradientQuadUIObject = new ArrayList<>();
		listAnchoredGradientQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, QuadMesh.class}, (Object[] arr) -> (UIObject) new AnchoredGradientQuadUIObject((String) arr[0], (QuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(AnchoredGradientQuadUIObject.class, listAnchoredGradientQuadUIObject);

		/*                 IndexedFlatQuadUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listIndexedFlatQuadUIObject = new ArrayList<>();
		listIndexedFlatQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new IndexedFlatQuadUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(IndexedFlatQuadUIObject.class, listIndexedFlatQuadUIObject);

		/*                 ScrollBarUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listScrollBarUIObject = new ArrayList<>();
		listScrollBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new ScrollBarUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(ScrollBarUIObject.class, listScrollBarUIObject);

		/*                 AnchoredFlatQuadUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listAnchoredFlatQuadUIObject = new ArrayList<>();
		listAnchoredFlatQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new AnchoredFlatQuadUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(AnchoredFlatQuadUIObject.class, listAnchoredFlatQuadUIObject);

		/*                 BuildingItemFlatQuadUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listBuildingItemFlatQuadUIObject = new ArrayList<>();
		listBuildingItemFlatQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new BuildingItemUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(BuildingItemUIObject.class, listBuildingItemFlatQuadUIObject);

		/*                 LayoutScrollDrivenUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listLayoutScrollDrivenUIObjectGroup = new ArrayList<>();
		listLayoutScrollDrivenUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Supplier.class, Direction.class, float.class, Layout.class, UIObject[].class}, (Object[] arr) -> (UIObject) new LayoutScrollDrivenUIObjectGroup((String) arr[0], (Supplier) arr[1], (Direction) arr[2], (float) arr[3], (Layout) arr[4], (UIObject[]) arr[5])));
		listLayoutScrollDrivenUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, Supplier.class, Direction.class, float.class, Layout.class, UIObject[].class}, (Object[] arr) -> (UIObject) new LayoutScrollDrivenUIObjectGroup((String) arr[0], (UIObjectGroup) arr[1], (Supplier) arr[2], (Direction) arr[3], (float) arr[4], (Layout) arr[5], (UIObject[]) arr[6])));
		listLayoutScrollDrivenUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Transform3D.class, Supplier.class, Direction.class, float.class, Layout.class, UIObject[].class}, (Object[] arr) -> (UIObject) new LayoutScrollDrivenUIObjectGroup((String) arr[0], (Transform3D) arr[1], (Supplier) arr[2], (Direction) arr[3], (float) arr[4], (Layout) arr[5], (UIObject[]) arr[6])));
		UI_OBJECT_CONSTRUCTORS.put(LayoutScrollDrivenUIObjectGroup.class, listLayoutScrollDrivenUIObjectGroup);

		/*                 BoundedUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listBoundedUIObjectGroup = new ArrayList<>();
		listBoundedUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Direction2d.class, UIObject[].class}, (Object[] arr) -> (UIObject) new BoundedUIObjectGroup((String) arr[0], (Layout) arr[1], (Direction2d) arr[2], (UIObject[]) arr[3])));
		listBoundedUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObjectGroup.class, Direction2d.class, UIObject[].class}, (Object[] arr) -> (UIObject) new BoundedUIObjectGroup((String) arr[0], (Layout) arr[1], (UIObjectGroup) arr[2], (Direction2d) arr[3], (UIObject[]) arr[4])));
		listBoundedUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Transform3D.class, Direction2d.class, UIObject[].class}, (Object[] arr) -> (UIObject) new BoundedUIObjectGroup((String) arr[0], (Layout) arr[1], (Transform3D) arr[2], (Direction2d) arr[3], (UIObject[]) arr[4])));
		UI_OBJECT_CONSTRUCTORS.put(BoundedUIObjectGroup.class, listBoundedUIObjectGroup);

		/*                 ParentUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listParentUIObjectGroup = new ArrayList<>();
		listParentUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ParentUIObjectGroup((String) arr[0], (Layout) arr[1], (UIObject[]) arr[2])));
		listParentUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObjectGroup.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ParentUIObjectGroup((String) arr[0], (Layout) arr[1], (UIObjectGroup) arr[2], (UIObject[]) arr[3])));
		listParentUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ParentUIObjectGroup((String) arr[0], (Layout) arr[1], (Transform3D) arr[2], (UIObject[]) arr[3])));
		UI_OBJECT_CONSTRUCTORS.put(ParentUIObjectGroup.class, listParentUIObjectGroup);

		/*                 FixedBoundsUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listFixedBoundsUIObjectGroup = new ArrayList<>();
		listFixedBoundsUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Direction2d.class, float.class, UIObject[].class}, (Object[] arr) -> (UIObject) new FixedBoundsUIObjectGroup((String) arr[0], (Layout) arr[1], (Direction2d) arr[2], (float) arr[3], (UIObject[]) arr[4])));
		listFixedBoundsUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Transform3D.class, Direction2d.class, float.class, UIObject[].class}, (Object[] arr) -> (UIObject) new FixedBoundsUIObjectGroup((String) arr[0], (Layout) arr[1], (Transform3D) arr[2], (Direction2d) arr[3], (float) arr[4], (UIObject[]) arr[5])));
		listFixedBoundsUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObjectGroup.class, Direction2d.class, float.class, UIObject[].class}, (Object[] arr) -> (UIObject) new FixedBoundsUIObjectGroup((String) arr[0], (Layout) arr[1], (UIObjectGroup) arr[2], (Direction2d) arr[3], (float) arr[4], (UIObject[]) arr[5])));
		UI_OBJECT_CONSTRUCTORS.put(FixedBoundsUIObjectGroup.class, listFixedBoundsUIObjectGroup);

		/*                 AnchoredLayoutUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listAnchoredLayoutUIObjectGroup = new ArrayList<>();
		listAnchoredLayoutUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObjectGroup.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredLayoutUIObjectGroup((String) arr[0], (Layout) arr[1], (UIObjectGroup) arr[2], (UIObject[]) arr[3])));
		listAnchoredLayoutUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredLayoutUIObjectGroup((String) arr[0], (Layout) arr[1], (Transform3D) arr[2], (UIObject[]) arr[3])));
		listAnchoredLayoutUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Anchor.class, Anchor.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredLayoutUIObjectGroup((String) arr[0], (Layout) arr[1], (Anchor) arr[2], (Anchor) arr[3], (UIObject[]) arr[4])));
		listAnchoredLayoutUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObjectGroup.class, Anchor.class, Anchor.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredLayoutUIObjectGroup((String) arr[0], (Layout) arr[1], (UIObjectGroup) arr[2], (Anchor) arr[3], (Anchor) arr[4], (UIObject[]) arr[5])));
		UI_OBJECT_CONSTRUCTORS.put(AnchoredLayoutUIObjectGroup.class, listAnchoredLayoutUIObjectGroup);

		/*                 FixedIntegerStatLine                 */
		final List<InternalConstructorFunction<UIObject>> listFixedIntegerStatLine = new ArrayList<>();
		listFixedIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObject[].class}, (Object[] arr) -> (UIObject) new FixedIntegerStatLine((String) arr[0], (UIObject[]) arr[1])));
		listFixedIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, float.class}, (Object[] arr) -> (UIObject) new FixedIntegerStatLine((String) arr[0], (float) arr[1])));
		listFixedIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, UIObject[].class}, (Object[] arr) -> (UIObject) new FixedIntegerStatLine((String) arr[0], (UIObjectGroup) arr[1], (UIObject[]) arr[2])));
		listFixedIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new FixedIntegerStatLine((String) arr[0], (Transform3D) arr[1], (UIObject[]) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(FixedIntegerStatLine.class, listFixedIntegerStatLine);

		/*                 IntegerStatLine                 */
		final List<InternalConstructorFunction<UIObject>> listIntegerStatLine = new ArrayList<>();
		listIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObject[].class}, (Object[] arr) -> (UIObject) new IntegerStatLine((String) arr[0], (UIObject[]) arr[1])));
		listIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new IntegerStatLine((String) arr[0], (Transform3D) arr[1], (UIObject[]) arr[2])));
		listIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, UIObject[].class}, (Object[] arr) -> (UIObject) new IntegerStatLine((String) arr[0], (UIObjectGroup) arr[1], (UIObject[]) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(IntegerStatLine.class, listIntegerStatLine);

		/*                 AnchoredProgressBarUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listAnchoredProgressBarUIObject = new ArrayList<>();
		listAnchoredProgressBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredProgressBarUIObject((String) arr[0], (UIObject[]) arr[1])));
		listAnchoredProgressBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, UIScene.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredProgressBarUIObject((String) arr[0], (UIScene) arr[1], (UIObject[]) arr[2])));
		listAnchoredProgressBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredProgressBarUIObject((String) arr[0], (Transform3D) arr[1], (UIObject[]) arr[2])));
		listAnchoredProgressBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredProgressBarUIObject((String) arr[0], (UIObjectGroup) arr[1], (UIObject[]) arr[2])));
		listAnchoredProgressBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, UIScene.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredProgressBarUIObject((String) arr[0], (UIScene) arr[1], (Transform3D) arr[2], (UIObject[]) arr[3])));
		listAnchoredProgressBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Anchor.class, Anchor.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredProgressBarUIObject((String) arr[0], (Anchor) arr[1], (Anchor) arr[2], (UIObject[]) arr[3])));
		listAnchoredProgressBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredProgressBarUIObject((String) arr[0], (UIObjectGroup) arr[1], (Transform3D) arr[2], (UIObject[]) arr[3])));
		listAnchoredProgressBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, UIScene.class, Transform3D.class, float.class, float.class}, (Object[] arr) -> (UIObject) new AnchoredProgressBarUIObject((String) arr[0], (UIScene) arr[1], (Transform3D) arr[2], (float) arr[3], (float) arr[4])));
		listAnchoredProgressBarUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, UIScene.class, Transform3D.class, Anchor.class, Anchor.class, float.class, float.class}, (Object[] arr) -> (UIObject) new AnchoredProgressBarUIObject((String) arr[0], (UIScene) arr[1], (Transform3D) arr[2], (Anchor) arr[3], (Anchor) arr[4], (float) arr[5], (float) arr[6])));
		UI_OBJECT_CONSTRUCTORS.put(AnchoredProgressBarUIObject.class, listAnchoredProgressBarUIObject);

		/*                 VolumeSliderUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listVolumeSliderUIObject = new ArrayList<>();
		listVolumeSliderUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new VolumeSliderUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(VolumeSliderUIObject.class, listVolumeSliderUIObject);
		DATA_PATH.put(VolumeSliderUIObject.class, "localization:slider.volume");

		/*                 PercentageIntTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listPercentageIntTextUIObject = new ArrayList<>();
		listPercentageIntTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new PercentageIntTextUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(PercentageIntTextUIObject.class, listPercentageIntTextUIObject);
		BUFFER_SIZE.put(PercentageIntTextUIObject.class, 10);
		DATA_PATH.put(PercentageIntTextUIObject.class, "localization:string-placeholder");

		/*                 SignedIntegerTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listSignedIntegerTextUIObject = new ArrayList<>();
		listSignedIntegerTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new SignedIntegerTextUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(SignedIntegerTextUIObject.class, listSignedIntegerTextUIObject);
		BUFFER_SIZE.put(SignedIntegerTextUIObject.class, 10);
		DATA_PATH.put(SignedIntegerTextUIObject.class, "localization:string-placeholder");

		/*                 OptionKeyUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listOptionKeyUIObject = new ArrayList<>();
		listOptionKeyUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new OptionKeyUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(OptionKeyUIObject.class, listOptionKeyUIObject);
		DATA_PATH.put(OptionKeyUIObject.class, "localization:string-placeholder");

		/*                 ExtAnchoredProgrammaticTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listExtAnchoredProgrammaticTextUIObject = new ArrayList<>();
		listExtAnchoredProgrammaticTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new ExtAnchoredProgrammaticTextUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(ExtAnchoredProgrammaticTextUIObject.class, listExtAnchoredProgrammaticTextUIObject);
		DATA_PATH.put(ExtAnchoredProgrammaticTextUIObject.class, "localization:string-placeholder");

		/*                 ExtAnchoredGradientQuadUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listExtAnchoredGradientQuadUIObject = new ArrayList<>();
		listExtAnchoredGradientQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, QuadMesh.class}, (Object[] arr) -> (UIObject) new ExtAnchoredGradientQuadUIObject((String) arr[0], (QuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(ExtAnchoredGradientQuadUIObject.class, listExtAnchoredGradientQuadUIObject);

		/*                 ExtAnchoredFlatQuadUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listExtAnchoredFlatQuadUIObject = new ArrayList<>();
		listExtAnchoredFlatQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new ExtAnchoredFlatQuadUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(ExtAnchoredFlatQuadUIObject.class, listExtAnchoredFlatQuadUIObject);

		/*                 IBAnchoredFlatQuadUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listIBAnchoredFlatQuadUIObject = new ArrayList<>();
		listIBAnchoredFlatQuadUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TexturedQuadMesh.class}, (Object[] arr) -> (UIObject) new IBAnchoredFlatQuadUIObject((String) arr[0], (TexturedQuadMesh) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(IBAnchoredFlatQuadUIObject.class, listIBAnchoredFlatQuadUIObject);

		/*                 OptionVolumeUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listOptionVolumeUIObjectGroup = new ArrayList<>();
		listOptionVolumeUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {}, (Object[] arr) -> (UIObject) new OptionVolumeUIObjectGroup()));
		listOptionVolumeUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {UIObjectGroup.class}, (Object[] arr) -> (UIObject) new OptionVolumeUIObjectGroup((UIObjectGroup) arr[0])));
		UI_OBJECT_CONSTRUCTORS.put(OptionVolumeUIObjectGroup.class, listOptionVolumeUIObjectGroup);

		/*                 OptionKeyUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listOptionKeyUIObjectGroup = new ArrayList<>();
		listOptionKeyUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {KeyOption.class, UIObjectGroup.class}, (Object[] arr) -> (UIObject) new OptionKeyUIObjectGroup((KeyOption) arr[0], (UIObjectGroup) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(OptionKeyUIObjectGroup.class, listOptionKeyUIObjectGroup);

		/*                 PaddingParentUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listPaddingParentUIObject = new ArrayList<>();
		listPaddingParentUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObject[].class}, (Object[] arr) -> (UIObject) new PaddingParentUIObject((String) arr[0], (Layout) arr[1], (UIObject[]) arr[2])));
		listPaddingParentUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObjectGroup.class, float.class}, (Object[] arr) -> (UIObject) new PaddingParentUIObject((String) arr[0], (Layout) arr[1], (UIObjectGroup) arr[2], (float) arr[3])));
		listPaddingParentUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Transform3D.class, float.class}, (Object[] arr) -> (UIObject) new PaddingParentUIObject((String) arr[0], (Layout) arr[1], (Transform3D) arr[2], (float) arr[3])));
		listPaddingParentUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObjectGroup.class, UIObject[].class}, (Object[] arr) -> (UIObject) new PaddingParentUIObject((String) arr[0], (Layout) arr[1], (UIObjectGroup) arr[2], (UIObject[]) arr[3])));
		listPaddingParentUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new PaddingParentUIObject((String) arr[0], (Layout) arr[1], (Transform3D) arr[2], (UIObject[]) arr[3])));
		UI_OBJECT_CONSTRUCTORS.put(PaddingParentUIObject.class, listPaddingParentUIObject);

		/*                 BuildingInfoUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listBuildingInfoUIObjectGroup = new ArrayList<>();
		listBuildingInfoUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {}, (Object[] arr) -> (UIObject) new BuildingInfoUIObjectGroup()));
		UI_OBJECT_CONSTRUCTORS.put(BuildingInfoUIObjectGroup.class, listBuildingInfoUIObjectGroup);

		/*                 FixedPBUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listFixedPBUIObjectGroup = new ArrayList<>();
		listFixedPBUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Direction2d.class, float.class, UIObject[].class}, (Object[] arr) -> (UIObject) new FixedPBUIObjectGroup((String) arr[0], (Layout) arr[1], (Direction2d) arr[2], (float) arr[3], (UIObject[]) arr[4])));
		listFixedPBUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObjectGroup.class, Direction2d.class, float.class, UIObject[].class}, (Object[] arr) -> (UIObject) new FixedPBUIObjectGroup((String) arr[0], (Layout) arr[1], (UIObjectGroup) arr[2], (Direction2d) arr[3], (float) arr[4], (UIObject[]) arr[5])));
		listFixedPBUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Transform3D.class, Direction2d.class, float.class, UIObject[].class}, (Object[] arr) -> (UIObject) new FixedPBUIObjectGroup((String) arr[0], (Layout) arr[1], (Transform3D) arr[2], (Direction2d) arr[3], (float) arr[4], (UIObject[]) arr[5])));
		UI_OBJECT_CONSTRUCTORS.put(FixedPBUIObjectGroup.class, listFixedPBUIObjectGroup);

		/*                 BuildingTabListUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listBuildingTabListUIObjectGroup = new ArrayList<>();
		listBuildingTabListUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {}, (Object[] arr) -> (UIObject) new BuildingTabListUIObjectGroup()));
		UI_OBJECT_CONSTRUCTORS.put(BuildingTabListUIObjectGroup.class, listBuildingTabListUIObjectGroup);

		/*                 AnchoredPBUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listAnchoredPBUIObjectGroup = new ArrayList<>();
		listAnchoredPBUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObjectGroup.class, Direction2d.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredPBUIObjectGroup((String) arr[0], (Layout) arr[1], (UIObjectGroup) arr[2], (Direction2d) arr[3], (UIObject[]) arr[4])));
		listAnchoredPBUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Transform3D.class, Direction2d.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredPBUIObjectGroup((String) arr[0], (Layout) arr[1], (Transform3D) arr[2], (Direction2d) arr[3], (UIObject[]) arr[4])));
		listAnchoredPBUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Direction2d.class, Anchor.class, Anchor.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredPBUIObjectGroup((String) arr[0], (Layout) arr[1], (Direction2d) arr[2], (Anchor) arr[3], (Anchor) arr[4], (UIObject[]) arr[5])));
		listAnchoredPBUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObjectGroup.class, Direction2d.class, Anchor.class, Anchor.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredPBUIObjectGroup((String) arr[0], (Layout) arr[1], (UIObjectGroup) arr[2], (Direction2d) arr[3], (Anchor) arr[4], (Anchor) arr[5], (UIObject[]) arr[6])));
		UI_OBJECT_CONSTRUCTORS.put(AnchoredPBUIObjectGroup.class, listAnchoredPBUIObjectGroup);

		/*                 BuildingTabUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listBuildingTabUIObjectGroup = new ArrayList<>();
		listBuildingTabUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, int.class, ColorMaterial.class}, (Object[] arr) -> (UIObject) new BuildingTabUIObjectGroup((String) arr[0], (int) arr[1], (ColorMaterial) arr[2])));
		UI_OBJECT_CONSTRUCTORS.put(BuildingTabUIObjectGroup.class, listBuildingTabUIObjectGroup);

		/*                 AnchoredParentUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listAnchoredParentUIObjectGroup = new ArrayList<>();
		listAnchoredParentUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObjectGroup.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredParentUIObjectGroup((String) arr[0], (Layout) arr[1], (UIObjectGroup) arr[2], (UIObject[]) arr[3])));
		listAnchoredParentUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredParentUIObjectGroup((String) arr[0], (Layout) arr[1], (Transform3D) arr[2], (UIObject[]) arr[3])));
		listAnchoredParentUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Anchor.class, Anchor.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredParentUIObjectGroup((String) arr[0], (Layout) arr[1], (Anchor) arr[2], (Anchor) arr[3], (UIObject[]) arr[4])));
		UI_OBJECT_CONSTRUCTORS.put(AnchoredParentUIObjectGroup.class, listAnchoredParentUIObjectGroup);

		/*                 BuildingPanelUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listBuildingPanelUIObjectGroup = new ArrayList<>();
		listBuildingPanelUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {}, (Object[] arr) -> (UIObject) new BuildingPanelUIObjectGroup()));
		UI_OBJECT_CONSTRUCTORS.put(BuildingPanelUIObjectGroup.class, listBuildingPanelUIObjectGroup);

		/*                 MarginAnchoredUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listMarginAnchoredUIObjectGroup = new ArrayList<>();
		listMarginAnchoredUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObjectGroup.class, UIObject[].class}, (Object[] arr) -> (UIObject) new MarginAnchoredUIObjectGroup((String) arr[0], (Layout) arr[1], (UIObjectGroup) arr[2], (UIObject[]) arr[3])));
		listMarginAnchoredUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new MarginAnchoredUIObjectGroup((String) arr[0], (Layout) arr[1], (Transform3D) arr[2], (UIObject[]) arr[3])));
		listMarginAnchoredUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Anchor.class, Anchor.class, float.class}, (Object[] arr) -> (UIObject) new MarginAnchoredUIObjectGroup((String) arr[0], (Layout) arr[1], (Anchor) arr[2], (Anchor) arr[3], (float) arr[4])));
		listMarginAnchoredUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Anchor.class, Anchor.class, UIObject[].class}, (Object[] arr) -> (UIObject) new MarginAnchoredUIObjectGroup((String) arr[0], (Layout) arr[1], (Anchor) arr[2], (Anchor) arr[3], (UIObject[]) arr[4])));
		listMarginAnchoredUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObjectGroup.class, Anchor.class, Anchor.class, float.class}, (Object[] arr) -> (UIObject) new MarginAnchoredUIObjectGroup((String) arr[0], (Layout) arr[1], (UIObjectGroup) arr[2], (Anchor) arr[3], (Anchor) arr[4], (float) arr[5])));
		listMarginAnchoredUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObjectGroup.class, Anchor.class, Anchor.class, UIObject[].class}, (Object[] arr) -> (UIObject) new MarginAnchoredUIObjectGroup((String) arr[0], (Layout) arr[1], (UIObjectGroup) arr[2], (Anchor) arr[3], (Anchor) arr[4], (UIObject[]) arr[5])));
		UI_OBJECT_CONSTRUCTORS.put(MarginAnchoredUIObjectGroup.class, listMarginAnchoredUIObjectGroup);

		/*                 AnchoredFixedIntegerStatLine                 */
		final List<InternalConstructorFunction<UIObject>> listAnchoredFixedIntegerStatLine = new ArrayList<>();
		listAnchoredFixedIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredFixedIntegerStatLine((String) arr[0], (UIObject[]) arr[1])));
		listAnchoredFixedIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, Anchor.class, Anchor.class}, (Object[] arr) -> (UIObject) new AnchoredFixedIntegerStatLine((String) arr[0], (Anchor) arr[1], (Anchor) arr[2])));
		listAnchoredFixedIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredFixedIntegerStatLine((String) arr[0], (UIObjectGroup) arr[1], (UIObject[]) arr[2])));
		listAnchoredFixedIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredFixedIntegerStatLine((String) arr[0], (Transform3D) arr[1], (UIObject[]) arr[2])));
		listAnchoredFixedIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, float.class, Anchor.class, Anchor.class}, (Object[] arr) -> (UIObject) new AnchoredFixedIntegerStatLine((String) arr[0], (float) arr[1], (Anchor) arr[2], (Anchor) arr[3])));
		UI_OBJECT_CONSTRUCTORS.put(AnchoredFixedIntegerStatLine.class, listAnchoredFixedIntegerStatLine);

		/*                 ExtAnchoredFixedIntegerStatLine                 */
		final List<InternalConstructorFunction<UIObject>> listExtAnchoredFixedIntegerStatLine = new ArrayList<>();
		listExtAnchoredFixedIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ExtAnchoredFixedIntegerStatLine((String) arr[0], (UIObject[]) arr[1])));
		listExtAnchoredFixedIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ExtAnchoredFixedIntegerStatLine((String) arr[0], (UIObjectGroup) arr[1], (UIObject[]) arr[2])));
		listExtAnchoredFixedIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ExtAnchoredFixedIntegerStatLine((String) arr[0], (Transform3D) arr[1], (UIObject[]) arr[2])));
		listExtAnchoredFixedIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, Anchor.class, Anchor.class, UIObject.class}, (Object[] arr) -> (UIObject) new ExtAnchoredFixedIntegerStatLine((String) arr[0], (Anchor) arr[1], (Anchor) arr[2], (UIObject) arr[3])));
		UI_OBJECT_CONSTRUCTORS.put(ExtAnchoredFixedIntegerStatLine.class, listExtAnchoredFixedIntegerStatLine);

		/*                 ExtAnchoredIntegerStatLine                 */
		final List<InternalConstructorFunction<UIObject>> listExtAnchoredIntegerStatLine = new ArrayList<>();
		listExtAnchoredIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ExtAnchoredIntegerStatLine((String) arr[0], (UIObject[]) arr[1])));
		listExtAnchoredIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, UIObjectGroup.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ExtAnchoredIntegerStatLine((String) arr[0], (UIObjectGroup) arr[1], (UIObject[]) arr[2])));
		listExtAnchoredIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, Transform3D.class, UIObject[].class}, (Object[] arr) -> (UIObject) new ExtAnchoredIntegerStatLine((String) arr[0], (Transform3D) arr[1], (UIObject[]) arr[2])));
		listExtAnchoredIntegerStatLine.add(new InternalConstructorFunction<>(new Class[] {String.class, Anchor.class, Anchor.class, UIObject.class}, (Object[] arr) -> (UIObject) new ExtAnchoredIntegerStatLine((String) arr[0], (Anchor) arr[1], (Anchor) arr[2], (UIObject) arr[3])));
		UI_OBJECT_CONSTRUCTORS.put(ExtAnchoredIntegerStatLine.class, listExtAnchoredIntegerStatLine);

		/*                 PercentageSignedIntTextUIObject                 */
		final List<InternalConstructorFunction<UIObject>> listPercentageSignedIntTextUIObject = new ArrayList<>();
		listPercentageSignedIntTextUIObject.add(new InternalConstructorFunction<>(new Class[] {String.class, TextEmitter.class}, (Object[] arr) -> (UIObject) new PercentageSignedIntTextUIObject((String) arr[0], (TextEmitter) arr[1])));
		UI_OBJECT_CONSTRUCTORS.put(PercentageSignedIntTextUIObject.class, listPercentageSignedIntTextUIObject);
		BUFFER_SIZE.put(PercentageSignedIntTextUIObject.class, 10);
		DATA_PATH.put(PercentageSignedIntTextUIObject.class, "localization:string-placeholder");

		/*                 AnchoredFixedPBUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listAnchoredFixedPBUIObjectGroup = new ArrayList<>();
		listAnchoredFixedPBUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Direction2d.class, float.class, Anchor.class, Anchor.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredFixedPBUIObjectGroup((String) arr[0], (Layout) arr[1], (Direction2d) arr[2], (float) arr[3], (Anchor) arr[4], (Anchor) arr[5], (UIObject[]) arr[6])));
		listAnchoredFixedPBUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, UIObjectGroup.class, Direction2d.class, float.class, Anchor.class, Anchor.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredFixedPBUIObjectGroup((String) arr[0], (Layout) arr[1], (UIObjectGroup) arr[2], (Direction2d) arr[3], (float) arr[4], (Anchor) arr[5], (Anchor) arr[6], (UIObject[]) arr[7])));
		listAnchoredFixedPBUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {String.class, Layout.class, Transform3D.class, Direction2d.class, float.class, Anchor.class, Anchor.class, UIObject[].class}, (Object[] arr) -> (UIObject) new AnchoredFixedPBUIObjectGroup((String) arr[0], (Layout) arr[1], (Transform3D) arr[2], (Direction2d) arr[3], (float) arr[4], (Anchor) arr[5], (Anchor) arr[6], (UIObject[]) arr[7])));
		UI_OBJECT_CONSTRUCTORS.put(AnchoredFixedPBUIObjectGroup.class, listAnchoredFixedPBUIObjectGroup);

		/*                 OptionsUIObjectGroup                 */
		final List<InternalConstructorFunction<UIObject>> listOptionsUIObjectGroup = new ArrayList<>();
		listOptionsUIObjectGroup.add(new InternalConstructorFunction<>(new Class[] {UIObjectGroup.class}, (Object[] arr) -> (UIObject) new OptionsUIObjectGroup((UIObjectGroup) arr[0])));
		UI_OBJECT_CONSTRUCTORS.put(OptionsUIObjectGroup.class, listOptionsUIObjectGroup);

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
