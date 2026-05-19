package corpsync.controlador;
import java.sql.DriverManager; 
import java.sql.Connection;
import java.sql.SQLException;

/**Clase ConexionBBDD: Simplemente se usa para crear la conexión a la base de datos de PostgreSQL de la plataforma. Guarda la conexion en un campo estatico 
 * y esta se comparte con la instancias de la clase AccesoADatosBBDD. Tiene campos para configurar la conexión y un métodos estático getters para devolver la conexion .**/
public class ConexionBBDD {

	public static String url = "jdbc:postgresql://aws-1-eu-central-1.pooler.supabase.com:5432/postgres" ;
	public static String referenciaPr =  ".mofuozleqotjlsavxfkv" ;
    public static Connection conexion ;
	public static Connection conexion2 ;
    
   


	public static Connection conectar(String usuario, String pwd){
		
		System.out.println("--->  ConexionBBDD.conectar()");
		Connection conector = null;
		Connection conector2 = null;
		try{
		conector = DriverManager.getConnection(url, usuario + referenciaPr ,pwd);
		System.out.println("¡¡¡¡¡¡  conexion2 establecida  ¡¡¡¡¡¡ Con user : " + usuario);
		conector2 = DriverManager.getConnection(url, "postgres" + referenciaPr ,pwd);
		System.out.println("¡¡¡¡¡¡  conexion1 establecida  ¡¡¡¡¡¡ ");
		
		}catch (SQLException e) {
			muestraErrorSQL( e );
			if (conector == null) { 		System.out.println("¡¡¡¡¡¡Herror de conexion.No se pudo establecer conexion"); return null; }
		}		
		conexion = conector;
		conexion2 = conector2;
		
		return conector;

	}


	public static Connection getConexion(){
		System.out.println("--->  ConexionBBDD.getConexion()");
		
		return conexion;
		//return c;
	}
	
	public static Connection getConexion2() {
		System.out.println("--->  ConexionBBDD.getConexion2()");
		return conexion2;
	}



	public static void cerrarConexion()  {
		try {
	 		  if (conexion != null && !conexion.isClosed()) {
	 			 conexion.close();
	                System.out.println("+++   Conexión cerrada     +++");
	            }			
		} catch (SQLException e) {
			muestraErrorSQL( e );
			
		}
		try {
	 		  if (conexion2 != null && !conexion2.isClosed()) {
	 			 conexion2.close();
	                System.out.println("+++   Conexión2 cerrada     +++");
	            }			
		} catch (SQLException e) {
			muestraErrorSQL( e );
			
		}
	}
	
	
	
	public static void muestraErrorSQL(SQLException e ) {
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("SQL Estado: " + e.getSQLState());
		System.out.println("SQL codigo especifico: " + e.getErrorCode());
		System.out.println("SQL ERROR mensaje: " +e.getMessage());
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("____________________________________________________________________________________________________________________");
		e.printStackTrace();
		System.out.println("____________________________________________________________________________________________________________________");
		
	}


	
	
}
