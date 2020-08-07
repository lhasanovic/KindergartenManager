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
	Problem {
		@Override
		public String toString() {
			return "Having a problem";
		}
	},
	Ready_to_pickup {
		@Override
		public String toString() {
			return "Ready to pickup";
		}
	},
	Not_present {
		@Override
		public String toString() {
			return "Not present";
		}
	}
}
