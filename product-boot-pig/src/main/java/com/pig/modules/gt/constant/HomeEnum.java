package com.pig.modules.gt.constant;

public class HomeEnum {

    /**
     * 验证状态枚举
     */
    public enum PostTypeEnum{

        LIKES(1,"likes"), //点赞
        UNLIKE(2,"unlike"),//取消点赞
        SHARES(3,"shares"),//转贴
        UNSHARE(4,"unshare");//取消转贴

        PostTypeEnum(Integer key,String value){
            this.key = key;
            this.value = value;
        }

        private Integer key;

        private String value;

        public Integer getKey() {
            return key;
        }

        public void setKey(Integer key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 帖子种类
     */
    public enum NewsTypeEnum{

        HOME(1,"home"), //Home主页
        LIVE(2,"live");//直播


        NewsTypeEnum(Integer key,String value){
            this.key = key;
            this.value = value;
        }

        private Integer key;

        private String value;

        public Integer getKey() {
            return key;
        }

        public void setKey(Integer key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
