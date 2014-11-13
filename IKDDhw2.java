/*================================================================================================
	Author: ªL®iµ¾  Dept: EE (Senior) StudentID: E24019067 
	File name: IKDDhw2.java  
	Associated files: ConsoleIn.java, postgresql-9.3-1102.jdbc3.jar
	Program function: Search twitter post with input query 
	Input: query string
	Output: Text containing query string, user name, user id (display order is sorted by user id)     
=================================================================================================*/
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class IKDDhw2
{
	public static void main(String[] args)
    {		
		Connection c = null;
		Statement stmt = null;
		final int TEXTRANGE = 50;
		try 
		{
			System.out.println("Enter query string on twitter");
			String query = ConsoleIn.readLine();
			/* Connect to iservDB */
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://iservdb.cloudopenlab.org.tw:5432/chlin6016_db_1787","chlin6016_user_1787", "9nm9vdpG");
			c.setAutoCommit(false);			
			System.out.println("Opened database successfully");
			/* Search Twitter with input keyword */
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM \"twitter\" WHERE q = \'" + query + "\' ORDER BY user_id;" );

			/* Display 1.user_name 2.user_id 3.text */			
			System.out.format("%-15s%-25s%-15s\n","user_id","user_name","text");
			System.out.println("================================================"
					+ "============================================");
			while ( rs.next() ) 
			{
				long id = rs.getLong("user_id");
				String name = rs.getString("user_name");
				String text  = rs.getString("text");
				
				if(text.length() > TEXTRANGE)
				{
					String temp1 = text.substring(0, TEXTRANGE);
					System.out.format("%-15d%-25s%-50s", id, name, temp1);
					int startPosition = TEXTRANGE;
					String remainder = text.substring( startPosition, text.length() );
					while(remainder.length() > TEXTRANGE)
					{
						String temp2 = remainder.substring(0, TEXTRANGE);
						System.out.format("\n%-40s%-50s", " ", temp2);
						remainder = remainder.substring( startPosition, remainder.length() );
					}
					System.out.format("\n%-40s%-50s\n", " ", remainder);
					System.out.println("------------------------------------------------"
							+ "--------------------------------------------");
				}
				else
				{
					System.out.format("%-15d%-25s%-50s\n", id, name, text);
					System.out.println("------------------------------------------------"
							+ "--------------------------------------------");
				}					
			}
			rs.close();
			stmt.close();
			c.close();
		} 
		catch (Exception e) 
		{
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
		}
		 System.out.println("Operation done successfully");		
	}
}