package mx.meido.rfcdoconline.listener;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * Application Lifecycle Listener implementation class MybatisListenner
 * 
 */
public class DruidListenner implements ServletContextListener {
	private DruidDataSource dataSource;

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext ctx = sce.getServletContext();
		Properties props = new Properties();
		try {
			ResourceBundle rb = ResourceBundle.getBundle("druid-config");
			for(String key : rb.keySet()){
				props.put(key, rb.getString(key));
			}
			dataSource = (DruidDataSource) DruidDataSourceFactory
					.createDataSource(props);
			ctx.setAttribute("dataSource", dataSource);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			Connection conn = dataSource.getConnection();
			Statement stat = conn.createStatement();
			stat.execute("SHUTDOWN");
			stat.close();
			conn.close();
			dataSource.close();
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}

}
