package ba.unsa.etf.rpr;

import java.util.ArrayList;

public class ChildDiary {
	private Child child;
	private ArrayList<DiaryEntry> diary;

	public ChildDiary(Child child) {
		this.child = child;
		diary = new ArrayList<>();
	}

	public ChildDiary(Child child, ArrayList<DiaryEntry> diary) {
		this.child = child;
		this.diary = diary;

		if(this.diary == null)
			this.diary = new ArrayList<>();
	}

	public Child getChild() {
		return child;
	}

	public void setChild(Child child) {
		this.child = child;
	}

	public ArrayList<DiaryEntry> getDiary() {
		return diary;
	}

	public void setDiary(ArrayList<DiaryEntry> diary) {
		this.diary = diary;
	}

	public void addDiaryEntry(DiaryEntry entry) {
		diary.add(entry);
	}
}
