/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pig.modules.system.entity.vo;


import lombok.Data;

/**
 * AuthInfo
 *
 * @author
 */
@Data
public class AuthInfo {
	private String accessToken;
	private String tokenType;
	private String refreshToken;
	private String avatar = "https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png";
	private String authority;
	private String userName;
	private String account;
	private String roleid;
	private String deptName;
	private long expiresIn;
	private String license = "powered by product";
	/**
	 * 授权的产品列表
	 */
	private String productList;
}
