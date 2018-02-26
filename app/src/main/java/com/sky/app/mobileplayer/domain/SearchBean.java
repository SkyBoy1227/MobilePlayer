package com.sky.app.mobileplayer.domain;

import java.util.List;

/**
 * Created with Android Studio.
 * 描述: 搜索bean对象
 * Date: 2018/2/26
 * Time: 15:42
 *
 * @author 晏琦云
 * @version ${VERSION}
 */
public class SearchBean {

    /**
     * flag : ok
     * pageNo : 1
     * pageSize : 20
     * wd : 毛主席
     * total : 200
     * items : [{"itemID":"VIDEbohCLMNMIbPEMWN1PGF3180126","itemTitle":"《军旅人生》 20180125 吕寿庆：一家四代守岛情","itemType":"columnvideo_flag","detailUrl":"http://tv.cntv.cn/video/C10530/88673aa94372464b9c0e56626eadbb9b","pubTime":"2018-01-26 04:11:30","keywords":"军旅人生,俞洁,吕寿庆,守岛","category":"","guid":"88673aa94372464b9c0e56626eadbb9b","videoLength":"","source":"","brief":"","photoCount":"0","sub_column_id":"","datecheck":"2018-01-26","itemImage":{"imgUrl1":"http://p3.img.cctvpic.com/fmspic/2018/01/26/88673aa94372464b9c0e56626eadbb9b-19.jpg?p=2&h=120"}}]
     */

    private String flag;
    private String pageNo;
    private String pageSize;
    private String wd;
    private String total;
    private List<ItemsBean> items;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * itemID : VIDEbohCLMNMIbPEMWN1PGF3180126
         * itemTitle : 《军旅人生》 20180125 吕寿庆：一家四代守岛情
         * itemType : columnvideo_flag
         * detailUrl : http://tv.cntv.cn/video/C10530/88673aa94372464b9c0e56626eadbb9b
         * pubTime : 2018-01-26 04:11:30
         * keywords : 军旅人生,俞洁,吕寿庆,守岛
         * category :
         * guid : 88673aa94372464b9c0e56626eadbb9b
         * videoLength :
         * source :
         * brief :
         * photoCount : 0
         * sub_column_id :
         * datecheck : 2018-01-26
         * itemImage : {"imgUrl1":"http://p3.img.cctvpic.com/fmspic/2018/01/26/88673aa94372464b9c0e56626eadbb9b-19.jpg?p=2&h=120"}
         */

        private String itemID;
        private String itemTitle;
        private String itemType;
        private String detailUrl;
        private String pubTime;
        private String keywords;
        private String category;
        private String guid;
        private String videoLength;
        private String source;
        private String brief;
        private String photoCount;
        private String sub_column_id;
        private String datecheck;
        private ItemImageBean itemImage;

        public String getItemID() {
            return itemID;
        }

        public void setItemID(String itemID) {
            this.itemID = itemID;
        }

        public String getItemTitle() {
            return itemTitle;
        }

        public void setItemTitle(String itemTitle) {
            this.itemTitle = itemTitle;
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public String getDetailUrl() {
            return detailUrl;
        }

        public void setDetailUrl(String detailUrl) {
            this.detailUrl = detailUrl;
        }

        public String getPubTime() {
            return pubTime;
        }

        public void setPubTime(String pubTime) {
            this.pubTime = pubTime;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getVideoLength() {
            return videoLength;
        }

        public void setVideoLength(String videoLength) {
            this.videoLength = videoLength;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getBrief() {
            return brief;
        }

        public void setBrief(String brief) {
            this.brief = brief;
        }

        public String getPhotoCount() {
            return photoCount;
        }

        public void setPhotoCount(String photoCount) {
            this.photoCount = photoCount;
        }

        public String getSub_column_id() {
            return sub_column_id;
        }

        public void setSub_column_id(String sub_column_id) {
            this.sub_column_id = sub_column_id;
        }

        public String getDatecheck() {
            return datecheck;
        }

        public void setDatecheck(String datecheck) {
            this.datecheck = datecheck;
        }

        public ItemImageBean getItemImage() {
            return itemImage;
        }

        public void setItemImage(ItemImageBean itemImage) {
            this.itemImage = itemImage;
        }

        public static class ItemImageBean {
            /**
             * imgUrl1 : http://p3.img.cctvpic.com/fmspic/2018/01/26/88673aa94372464b9c0e56626eadbb9b-19.jpg?p=2&h=120
             */

            private String imgUrl1;

            public String getImgUrl1() {
                return imgUrl1;
            }

            public void setImgUrl1(String imgUrl1) {
                this.imgUrl1 = imgUrl1;
            }
        }
    }
}
