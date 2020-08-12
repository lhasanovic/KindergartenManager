package ba.unsa.etf.rpr;

public class DiaryEntry {
	private ChildActivity activity;
	private String description;

	public DiaryEntry(ChildActivity activity, String description) {
		this.activity = activity;
		this.description = description;
	}

	public DiaryEntry(ChildActivity activity) {
		this.activity = activity;
	}

	public ChildActivity getActivity() {
		return activity;
	}

	public void setActivity(ChildActivity activity) {
		this.activity = activity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
