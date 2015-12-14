package dungen.pojos;

public class Monster {
	public String CR = "";
	public String name = "";
	public String XPvalue = "";
	public String type = "";
	public String alignment = "";
	public String source = "";
	public String size = "";

	// 0,Frog,0,Tiny,Beast,Unaligned,mm 322
	public Monster(String[] x) {
		CR = x[0];
		name = x[1];
		XPvalue = x[2];
		size = x[3];
		type = x[4];
		alignment = x[5];
		source = x[6];
	}

}
