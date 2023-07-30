import java.util.List;

public class note {
	final int x;
	final int y;
	final int width;
	final int height;
	final String colour;
	final String msg;
	int pin;
	String status;
	
	public note(int x, int y, int width, int height, String colour, String msg) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.colour = colour;
		this.msg = msg;
		this.pin = 0;
		this.status = "unpinned";
	}
	
	public void printNote() {
		System.out.printf("note data: %d %d %d %d ", this.x, this.y, this.width,this.height);
		System.out.printf("%s %s %d %s%n", this.colour,this.msg,this.pin, this.status);
	}
	
	public static String[] sendNotes(List<note> notes) {
		String[] tosend = new String[notes.size()];
		String parseNote = "";
		int p = 0;
		for(note n : notes) {
			parseNote = String.format("%d %d %d %d %s %s %d %s", n.x,n.y,n.width,n.height,n.colour,n.msg,n.pin, n.status);
			tosend[p] = parseNote;
			p++;
		}
		return tosend;
	}
	
	public boolean coordInNote(int x, int y) {
		if((x >= this.x && x <= this.x + this.width) && (y >= this.y && x <= this.y + this.height)) {
			return true;
		}
		return false;
	}
}
