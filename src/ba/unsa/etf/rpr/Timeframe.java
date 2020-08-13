package ba.unsa.etf.rpr;

public enum Timeframe {
	All_time {
		@Override
		public String toString() {
			return "All time";
		}
	},
	This_year {
		@Override
		public String toString() {
			return "This year";
		}
	},
	Last_30_days {
		@Override
		public String toString() {
			return "Last 30 days";
		}
	},
	Last_7_days {
		@Override
		public String toString() {
			return "Last 7 days";
		}
	},
	Today
}
