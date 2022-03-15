package kr.soldesk;

public class ExcelVO {

	private String title;	
	private String author;	
	private String company;
	private String isbn;
	private String imageUrl;

	public ExcelVO(String title, String author, String company) {
		this(title, author, company, null, null);
	}
	
	public ExcelVO(String title, String author, String company, String isbn, String imageUrl) {
		super();
		this.title = title;
		this.author = author;
		this.company = company;
		this.isbn = isbn;
		this.imageUrl = imageUrl;
	}

	
	//getters
	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getCompany() {
		return company;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	//setters
	public void setTitle(String title) {
		this.title = title;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "ExcelVO [title=" + title + ", author=" + author + ", company=" + company + ", isbn=" + isbn + ", imageUrl="
				+ imageUrl + "]";
	}


	
}
