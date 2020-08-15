package ba.unsa.etf.rpr;

import java.util.ResourceBundle;

public enum Timeframe {
	All_time {
		@Override
		public String toString() {
			return ResourceBundle.getBundle("Translate").getString("all_time");
		}
	},
	This_year {
		@Override
		public String toString() {
			return ResourceBundle.getBundle("Translate").getString("this_year");
		}
	},
	Last_30_days {
		@Override
		public String toString() {
			return ResourceBundle.getBundle("Translate").getString("last_30_days");
		}
	},
	Last_7_days {
		@Override
		public String toString() {
			return ResourceBundle.getBundle("Translate").getString("last_7_days");
		}
	},
	Today {
		@Override
		public String toString() {
			return ResourceBundle.getBundle("Translate").getString("today");
		}
	}
}
