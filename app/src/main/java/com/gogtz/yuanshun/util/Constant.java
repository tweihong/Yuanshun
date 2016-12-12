/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package gogtz.scan.util;


/**常量工具类
 * @author Lemon
 * @warn TODO 修改里面所有常量
 */
public class Constant {

	public static final String APP_OFFICIAL_WEBSITE = "https://github.com/TommyLemon/ZBLibrary";
	public static final String APP_OFFICIAL_WEIBO = "weibo.com/5225556360";
	public static final String APP_OFFICIAL_BLOG = "http://my.oschina.net/u/2437072";
	public static final String APP_OFFICIAL_EMAIL = "1184482681@qq.com";
	public static final String APP_DOWNLOAD_WEBSITE = "http://files.cnblogs.com/files/tommylemon/ZBLibraryDemoApp.apk";

	public static final String APP_DEVELOPER_WEBSITE = "https://github.com/TommyLemon";

	/**
	 * 二维码请求的type
	 */
	public static final String REQUEST_SCAN_TYPE="type";
	/**
	 * 普通类型，扫完即关闭
	 */
	public static final int REQUEST_SCAN_TYPE_COMMON=0;
	/**
	 * 服务商登记类型，扫描
	 */
	public static final int REQUEST_SCAN_TYPE_REGIST=1;


	/**
	 * 扫描类型
	 * 条形码或者二维码：REQUEST_SCAN_MODE_ALL_MODE
	 * 条形码： REQUEST_SCAN_MODE_BARCODE_MODE
	 * 二维码：REQUEST_SCAN_MODE_QRCODE_MODE
	 *
	 */
	public static final String REQUEST_SCAN_MODE="ScanMode";
	/**
	 * 条形码： REQUEST_SCAN_MODE_BARCODE_MODE
	 */
	public static final int REQUEST_SCAN_MODE_BARCODE_MODE = 0X100;
	/**
	 * 二维码：REQUEST_SCAN_MODE_ALL_MODE
	 */
	public static final int REQUEST_SCAN_MODE_QRCODE_MODE = 0X200;
	/**
	 * 条形码或者二维码：REQUEST_SCAN_MODE_ALL_MODE
	 */
	public static final int REQUEST_SCAN_MODE_ALL_MODE = 0X300;

}
