package dnbrdf;

	public class emp {
		int cuid			= 0;
		String lastname 	= "";
		String firstname 	= "";
		String degree 		= "";
		String faculty		= "";
		int dnbautorid		= 0;
		int birth			= 0;
		
		public emp(int cuid, String lastname, String firstname, String degree, String faculty, int dnbautorid, int birth) 
		{
			this.cuid = cuid;
			this.lastname = lastname;
			this.firstname = firstname;
			this.degree = degree;
			this.faculty = faculty;
			this.dnbautorid = dnbautorid;
			this.birth = birth;
		}

		public String toString() 
		{
			return 	"cuid: " 		+ cuid + 
					" lastname: "	+ lastname + 
					" firstname: "	+ firstname +
					" degree: "		+ degree +
					" faculty: "	+ faculty +
					" dnbautorid: "	+ dnbautorid +
					" birth: "		+ birth;
		}
	}

