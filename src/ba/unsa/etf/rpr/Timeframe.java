package ba.unsa.etf.rpr;

public enum Timeframe {
	Today,
	This_week {
		@Override
		public String toString() {
			return "This week";
		}
	},
	This_month {
		@Override
		public String toString() {
			return "This month";
		}
	},
	This_year {
		@Override
		public String toString() {
			return "This year";
		}
	},
	All_time {
		@Override
		public String toString() {
			return "All time";
		}
	}
}
