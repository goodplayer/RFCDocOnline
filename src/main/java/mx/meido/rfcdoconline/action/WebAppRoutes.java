package mx.meido.rfcdoconline.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import mx.meido.rfcdoconline.action.utils.BaseAction;
import mx.meido.rfcdoconline.model.Rfc;
import mx.meido.rfcdoconline.model.RfcPage;
import mx.meido.rfcdoconline.util.RfcTxtParser;

import com.ghosthack.turismo.multipart.MultipartFilter;
import com.ghosthack.turismo.multipart.MultipartRequest;
import com.ghosthack.turismo.routes.RoutesList;

public class WebAppRoutes extends RoutesList {

	@Override
	protected void map() {
		//列出所有Rfc文档 - 第1页
		get("/rfc", new BaseAction() {
			@Override
			public void run() {
				try {
					int total = dbquery().query("SELECT count(1) FROM rfc", new ResultSetHandler<Integer>(){
						@Override
						public Integer handle(ResultSet rs) throws SQLException {
							if(!rs.next())
								return 0;
							return rs.getInt(1);
					}});
					List<Rfc> list = dbquery().query("SELECT * FROM rfc LIMIT ?,?", new BeanListHandler<Rfc>(Rfc.class), 0,NUM_PER_PAGE);
					int curpage = 1;
					int maxpage = total/NUM_PER_PAGE + (total%NUM_PER_PAGE>0?1:0);

					req().setAttribute("list", list);
					req().setAttribute("total", total);
					req().setAttribute("curpage", curpage);
					req().setAttribute("maxpage", maxpage);
					
					jsp("/WEB-INF/listAllRfc.jsp");
				} catch (Exception e) {
					e.printStackTrace();
					jsp("/WEB-INF/error.jsp");
				}
			}
		});
		//列出所有Rfc文档 - 第N页
		get("/rfc/page/:page", new BaseAction() {
			@Override
			public void run() {
				try {
					String pageStr = params("page");
					int page = 0;
					try {
						page = Integer.parseInt(pageStr);
					} catch (NumberFormatException e) {
						alias("/app/rfc");
						return;
					}
					
					int total = dbquery().query("SELECT count(1) FROM rfc", new ResultSetHandler<Integer>(){
						@Override
						public Integer handle(ResultSet rs) throws SQLException {
							if(!rs.next())
								return 0;
							return rs.getInt(1);
					}});
					List<Rfc> list = dbquery().query("SELECT * FROM rfc LIMIT ?,?", new BeanListHandler<Rfc>(Rfc.class), NUM_PER_PAGE*(page-1),NUM_PER_PAGE);
					int curpage = page;
					int maxpage = total/NUM_PER_PAGE + (total%NUM_PER_PAGE>0?1:0);

					req().setAttribute("list", list);
					req().setAttribute("total", total);
					req().setAttribute("curpage", curpage);
					req().setAttribute("maxpage", maxpage);
					
					jsp("/WEB-INF/listAllRfc.jsp");
				} catch (Exception e) {
					e.printStackTrace();
					jsp("/WEB-INF/error.jsp");
				}
			}
		});
		//查看指定RFC文档概览
		get("/rfc/:id", new BaseAction() {
			public void run() {
				String ids = params("id");
				int id;
				try {
					id = Integer.parseInt(ids);
				} catch (NumberFormatException e) {
					alias("/app/rfc");
					return;
				}
				
				try {
					Rfc rfc = dbquery().query("SELECT * FROM rfc WHERE id = ?", new BeanHandler<Rfc>(Rfc.class), id);
					req().setAttribute("rfc", rfc);
					jsp("/WEB-INF/rfcdetail.jsp");
				} catch (SQLException e) {
					e.printStackTrace();
					jsp("/WEB-INF/error.jsp");
				}
				
			}
		});
		//查看指定RFC文档指定页
		get("/rfc/:id/:page", new BaseAction() {
			@Override
			public void run() {
				String ids = params("id");
				String pageStr = params("page");
				int id = 0;
				int page = 0;
				try {
					id = Integer.parseInt(ids);
					page = Integer.parseInt(pageStr);
				} catch (Exception e) {
					alias("/app/rfc");
					return;
				}
				
				try {
					int maxpage = dbquery().query("SELECT count(1) FROM rfcpage WHERE id = ?", new ResultSetHandler<Integer>(){
						@Override
						public Integer handle(ResultSet rs) throws SQLException {
							if(!rs.next())
								return 0;
							return rs.getInt(1);
					}}, id);
					int curpage = page;
					RfcPage rfcPage = dbquery().query("SELECT * FROM rfcpage WHERE id = ? and pageNum = ?", new BeanHandler<RfcPage>(RfcPage.class), id, curpage);
					String content = (rfcPage == null) ? "":rfcPage.getContent();
					
					req().setAttribute("id", ids);
					req().setAttribute("content", content);
					req().setAttribute("curpage", curpage);
					req().setAttribute("maxpage", maxpage);
					jsp("/WEB-INF/rfcpage.jsp");
				} catch (SQLException e) {
					e.printStackTrace();
					jsp("/WEB-INF/error.jsp");
				}
			}
		});
		//rfc管理页面
		get("/rfcManage", new BaseAction() {
			@Override
			public void run() {
				//TODO 
				print("page /rfcManage");
			}
		});
		//开始处理rfc文档
		post("/rfcManage/doRfc", new BaseAction() {
			@Override
			public void run() {
				//TODO 
				print("page /rfcManage/doRfc");
			}
		});
		//上传rfc文档页面
		get("/rfcUpload", new BaseAction() {
			@Override
			public void run() {
				jsp("/WEB-INF/upload.jsp");
			}
		});
		//处理上传的rfc文档
		post("/rfcUpload", new BaseAction() {
			@Override
			public void run() {
				try {
					//1、获取表单数据
					
					MultipartRequest request = MultipartFilter.wrapAndParse(req());

			        String[] ids = request.getParameterValues("id");
			        String[] title = request.getParameterValues("title");
			        String[] rfcfile = request.getParameterValues("rfcfile");
			        byte[] rfcfileBytes = (byte[]) request.getAttribute("rfcfile");
			        
			        //2、验证表单
			        //3、成功动作，失败动作
			        if(ids[0] != null && ids[0].length() > 0
			        	&& title[0] != null && title[0].length() > 0
			        	&& rfcfile[1] != null && rfcfile[1].length() > 0
			        	&& rfcfileBytes.length > 0){
			        	
			        	int id = Integer.parseInt(ids[0]);
			        	
			        	final Rfc rfc = new Rfc();
			        	rfc.setAuthor("xxx");
			        	rfc.setId(id);
			        	rfc.setOrigInfo("xxx");
			        	rfc.setTitle(title[0]);
			        	
			        	final List<RfcPage> pages = PARSER.parseRfcBytes(rfcfileBytes, id);
			        	
			        	dbtrans(new SimpleTransManager() {
			        		private static final String DELETE_PRE_RFC = "DELETE FROM rfc WHERE id = ?";
			        		private static final String INSERT_RFC = "INSERT INTO rfc(id, title, author, origInfo) VALUES(?, ?, ?, ?)";
			        		private static final String DELETE_PRE_PAGES = "DELETE FROM rfcpage WHERE id = ?";
			        		private static final String INSERT_RFCPAGE = "INSERT INTO rfcpage(id, pageNum, content) VALUES(?, ?, ?)";
			        		@Override
			        		protected void doDb(Connection conn) throws SQLException {
			        			QueryRunner queryRunner = createQueryRunner();
			        			queryRunner.update(conn, DELETE_PRE_RFC, rfc.getId());
			        			queryRunner.update(conn, INSERT_RFC, rfc.getId(), rfc.getTitle(), rfc.getAuthor(), rfc.getOrigInfo());
			        			queryRunner.update(conn, DELETE_PRE_PAGES, rfc.getId());
			        			for(RfcPage rfcPage : pages){
			        				queryRunner.update(conn, INSERT_RFCPAGE, rfcPage.getId(), rfcPage.getPageNum(), rfcPage.getContent());
			        			}
			        		}
			        	});
			        	
			        	req().setAttribute("result", UPLOAD_FORM_SUCCESS);
			        }else{
			        	req().setAttribute("result", UPLOAD_FORM_ERROR);
			        }
			        
				} catch (Exception e) {
		        	req().setAttribute("result", UPLOAD_FORM_ERROR);
				}
				
				//4、根据成功失败进行结果反馈
				jsp("/WEB-INF/upload.jsp");
			}
		});
	}
	
	private static final int NUM_PER_PAGE = 10;
	
	private static final RfcTxtParser PARSER = new RfcTxtParser();
	
	private static final String UPLOAD_FORM_ERROR = "表单格式不正确,上传失败";
	private static final String UPLOAD_FORM_SUCCESS = "上传成功";
}
