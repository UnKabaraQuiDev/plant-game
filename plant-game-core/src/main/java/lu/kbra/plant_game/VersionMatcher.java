package lu.kbra.plant_game;

import java.util.Objects;

import lu.pcy113.pclib.PCUtils;

import lu.kbra.plant_game.VersionMatcher.BoundsVersionMatcher;
import lu.kbra.plant_game.VersionMatcher.RangeVersionMatcher;
import lu.kbra.plant_game.VersionMatcher.StrictVersionMatcher;

sealed public interface VersionMatcher permits StrictVersionMatcher, RangeVersionMatcher, BoundsVersionMatcher {

	boolean matches(String v);

	public static final class StrictVersionMatcher implements VersionMatcher {

		private final String target;

		public StrictVersionMatcher(final String target) {
			this.target = target;
		}

		@Override
		public boolean matches(final String v) {
			return PCUtils.compareVersion(v, this.target) == 0;
		}

		public String getTarget() {
			return this.target;
		}

		@Override
		public String toString() {
			return "StrictVersionMatcher@" + System.identityHashCode(this) + " [target=" + this.target + "]";
		}

	}

	public static final class RangeVersionMatcher implements VersionMatcher {

		private final String targetLower;
		private final String targetUpper;

		public RangeVersionMatcher(final String targetLower, final String targetUpper) {
			this.targetLower = targetLower;
			this.targetUpper = targetUpper;
		}

		@Override
		public boolean matches(final String v) {
			return PCUtils.compareVersion(v, this.targetLower) > 0 && PCUtils.compareVersion(v, this.targetUpper) < 0;
		}

		public String getTargetLower() {
			return this.targetLower;
		}

		public String getTargetUpper() {
			return this.targetUpper;
		}

		@Override
		public String toString() {
			return "RangeVersionMatcher@" + System.identityHashCode(this) + " [targetLower=" + this.targetLower + ", targetUpper="
					+ this.targetUpper + "]";
		}

	}

	public static final class BoundsVersionMatcher implements VersionMatcher {

		public static enum BoundsDirection {
			UPPER, LOWER, UPPER_INCL, LOWER_INCL;
		}

		private final String target;
		private final BoundsDirection dir;

		public BoundsVersionMatcher(final String target, final BoundsDirection dir) {
			this.target = target;
			this.dir = dir;
		}

		@Override
		public boolean matches(final String v) {
			return switch (this.dir) {
			case UPPER -> PCUtils.compareVersion(v, this.target) < 0;
			case LOWER -> PCUtils.compareVersion(v, this.target) > 0;
			case UPPER_INCL -> PCUtils.compareVersion(v, this.target) <= 0;
			case LOWER_INCL -> PCUtils.compareVersion(v, this.target) >= 0;
			default -> throw new IllegalArgumentException(Objects.toString(this.dir));
			};
		}

		public String getTarget() {
			return this.target;
		}

		public BoundsDirection getDir() {
			return this.dir;
		}

		@Override
		public String toString() {
			return "BoundsVersionMatcher@" + System.identityHashCode(this) + " [target=" + this.target + ", dir=" + this.dir + "]";
		}

	}

}