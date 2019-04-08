package sjoshi11_java_part3;

public class DictTable {

	public String token;
	public String docFreq;
	public String offset;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((docFreq == null) ? 0 : docFreq.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DictTable other = (DictTable) obj;
		if (docFreq == null) {
			if (other.docFreq != null)
				return false;
		} else if (!docFreq.equals(other.docFreq))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "DictTable [token=" + token + ", docFreq=" + docFreq + ", offset=" + offset + "]";
	}

}
