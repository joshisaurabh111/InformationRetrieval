package sjoshi11_java_part3;

public class DocumentTable {
	public String filename;
	public String title;
	public String reviewer;
	public String rate;
	public String snippet;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filename == null) ? 0 : filename.hashCode());
		return result;
	}
	@Override
	public String toString() {
		return "[filename=" + filename + ", title=" + title + ", reviewer=" + reviewer + ", rate=" + rate
				+ ", snippet=" + snippet + "]\n";
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentTable other = (DocumentTable) obj;
		if (filename == null) {
			if (other.filename != null)
				return false;
		} else if (!filename.equals(other.filename))
			return false;
		return true;
	}
	
}
