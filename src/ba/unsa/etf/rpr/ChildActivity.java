package ba.unsa.etf.rpr;

import java.util.ResourceBundle;

public enum ChildActivity {
	Sleeping {
		@Override
		public String toString() {
			return ResourceBundle.getBundle("Translate").getString("sleeping");
		}
	},
	Playing {
		@Override
		public String toString() {
			return ResourceBundle.getBundle("Translate").getString("playing");
		}
	},
	Group_activity {
		@Override
		public String toString() {
			return ResourceBundle.getBundle("Translate").getString("group_activity");
		}
	},
	Eating {
		@Override
		public String toString() {
			return ResourceBundle.getBundle("Translate").getString("eating");
		}
	},
	Crying {
		@Override
		public String toString() {
			return ResourceBundle.getBundle("Translate").getString("crying");
		}
	},
	Problem {
		@Override
		public String toString() {
			return ResourceBundle.getBundle("Translate").getString("problem");
		}
	},
	Ready_to_pickup {
		@Override
		public String toString() {
			return ResourceBundle.getBundle("Translate").getString("ready_to_pickup");
		}
	},
	Not_present {
		@Override
		public String toString() {
			return ResourceBundle.getBundle("Translate").getString("not_present");
		}
	};

	public static String getEnumName(ChildActivity ca) {
		switch (ca) {
			case Sleeping:
				return "Sleeping";
			case Crying:
				return "Crying";
			case Eating:
				return "Eating";
			case Playing:
				return "Playing";
			case Problem:
				return "Problem";
			case Not_present:
				return "Not_present";
			case Group_activity:
				return "Group_activity";
			case Ready_to_pickup:
				return "Ready_to_pickup";
			default:
				return "";
		}
	}
}
