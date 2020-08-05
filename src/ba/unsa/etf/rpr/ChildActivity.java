package ba.unsa.etf.rpr;

public enum ChildActivity {
	Sleeping,
	Playing,
	Group_activity {
		@Override
		public String toString() {
			return "Group activity";
		}
	},
	Eating,
	Crying,
	Problem,
	Ready_to_pickup {
		@Override
		public String toString() {
			return "Ready to pickup";
		}
	},
	NOT_PRESENT {
		@Override
		public String toString() {
			return "Not present";
		}
	}
}
