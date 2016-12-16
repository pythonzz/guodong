package com.guodong.sun.guodong.entity.picture;

import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */

public class Picture {

    private String message;
    private DataBeanX data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {

        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {

            private GroupBean group;
            private int type;
            private List<?> comments;

            public GroupBean getGroup() {
                return group;
            }

            public void setGroup(GroupBean group) {
                this.group = group;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public List<?> getComments() {
                return comments;
            }

            public void setComments(List<?> comments) {
                this.comments = comments;
            }

            public static class GroupBean {

                private UserBean user;
                private String text;
                private long id;
                private String share_url;
                private String content;
                private int comment_count;
                private int media_type;
                private int share_count;
                private LargeImageBean large_image;
                private String category_name;
                private int bury_count;
                private boolean is_anonymous;
                private int repin_count;
                private boolean is_neihan_hot;
                private int digg_count;
                private long group_id;
                private MiddleImageBean middle_image;
                private int is_multi_image;
                private int is_gif;
                private List<ThumbImageList> thumb_image_list;
                private List<ThumbImageList> large_image_list;

                public int getIs_multi_image() {
                    return is_multi_image;
                }

                public void setIs_multi_image(int is_multi_image) {
                    this.is_multi_image = is_multi_image;
                }

                public int getIs_gif() {
                    return is_gif;
                }

                public void setIs_gif(int is_gif) {
                    this.is_gif = is_gif;
                }

                public List<ThumbImageList> getThumb_image_list() {
                    return thumb_image_list;
                }

                public void setThumb_image_list(List<ThumbImageList> thumb_image_list) {
                    this.thumb_image_list = thumb_image_list;
                }

                public List<ThumbImageList> getLarge_image_list() {
                    return large_image_list;
                }

                public void setLarge_image_list(List<ThumbImageList> large_image_list) {
                    this.large_image_list = large_image_list;
                }

                public UserBean getUser() {
                    return user;
                }

                public void setUser(UserBean user) {
                    this.user = user;
                }

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
                }

                public String getShare_url() {
                    return share_url;
                }

                public void setShare_url(String share_url) {
                    this.share_url = share_url;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public int getComment_count() {
                    return comment_count;
                }

                public void setComment_count(int comment_count) {
                    this.comment_count = comment_count;
                }

                public int getMedia_type() {
                    return media_type;
                }

                public void setMedia_type(int media_type) {
                    this.media_type = media_type;
                }

                public int getShare_count() {
                    return share_count;
                }

                public void setShare_count(int share_count) {
                    this.share_count = share_count;
                }

                public LargeImageBean getLarge_image() {
                    return large_image;
                }

                public void setLarge_image(LargeImageBean large_image) {
                    this.large_image = large_image;
                }

                public String getCategory_name() {
                    return category_name;
                }

                public void setCategory_name(String category_name) {
                    this.category_name = category_name;
                }

                public int getBury_count() {
                    return bury_count;
                }

                public void setBury_count(int bury_count) {
                    this.bury_count = bury_count;
                }

                public boolean isIs_anonymous() {
                    return is_anonymous;
                }

                public void setIs_anonymous(boolean is_anonymous) {
                    this.is_anonymous = is_anonymous;
                }

                public int getRepin_count() {
                    return repin_count;
                }

                public void setRepin_count(int repin_count) {
                    this.repin_count = repin_count;
                }

                public boolean isIs_neihan_hot() {
                    return is_neihan_hot;
                }

                public void setIs_neihan_hot(boolean is_neihan_hot) {
                    this.is_neihan_hot = is_neihan_hot;
                }

                public int getDigg_count() {
                    return digg_count;
                }

                public void setDigg_count(int digg_count) {
                    this.digg_count = digg_count;
                }

                public long getGroup_id() {
                    return group_id;
                }

                public void setGroup_id(long group_id) {
                    this.group_id = group_id;
                }

                public MiddleImageBean getMiddle_image() {
                    return middle_image;
                }

                public void setMiddle_image(MiddleImageBean middle_image) {
                    this.middle_image = middle_image;
                }

                public static class UserBean {

                    private String name;
                    private String avatar_url;

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getAvatar_url() {
                        return avatar_url;
                    }

                    public void setAvatar_url(String avatar_url) {
                        this.avatar_url = avatar_url;
                    }
                }

                public static class LargeImageBean {

                    private List<UrlListBean> url_list;

                    public List<UrlListBean> getUrl_list() {
                        return url_list;
                    }

                    public void setUrl_list(List<UrlListBean> url_list) {
                        this.url_list = url_list;
                    }

                    public static class UrlListBean {
                        private String url;

                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                        }
                    }
                }

                public static class MiddleImageBean {

                    private int width;
                    private int r_height;
                    private int r_width;
                    private String uri;
                    private int height;
                    private List<UrlListBeanX> url_list;

                    public int getWidth() {
                        return width;
                    }

                    public void setWidth(int width) {
                        this.width = width;
                    }

                    public int getR_height() {
                        return r_height;
                    }

                    public void setR_height(int r_height) {
                        this.r_height = r_height;
                    }

                    public int getR_width() {
                        return r_width;
                    }

                    public void setR_width(int r_width) {
                        this.r_width = r_width;
                    }

                    public String getUri() {
                        return uri;
                    }

                    public void setUri(String uri) {
                        this.uri = uri;
                    }

                    public int getHeight() {
                        return height;
                    }

                    public void setHeight(int height) {
                        this.height = height;
                    }

                    public List<UrlListBeanX> getUrl_list() {
                        return url_list;
                    }

                    public void setUrl_list(List<UrlListBeanX> url_list) {
                        this.url_list = url_list;
                    }

                    public static class UrlListBeanX {

                        private String url;

                        public String getUrl() {
                            return url;
                        }

                        public void setUrl(String url) {
                            this.url = url;
                        }
                    }
                }
            }
        }
    }
}
