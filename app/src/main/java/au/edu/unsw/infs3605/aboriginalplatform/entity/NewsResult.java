package au.edu.unsw.infs3605.aboriginalplatform.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class NewsResult {

    private List<News> articles;
    private String status;
    private Integer totalResults;

    public List<News> getArticles() {
        return articles;
    }

    public void setArticles(List<News> articles) {
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    @Override
    public String toString() {
        return "NewsResult{" +
                "articles=" + articles +
                ", status='" + status + '\'' +
                ", totalResults=" + totalResults +
                '}';
    }

    public static class News implements Parcelable {

        private String author;
        private String content;
        private String description;
        private String publishedAt;
        private SourceBean source;
        private String title;
        private String url;
        private String urlToImage;

        public News() {
        }


        public static class SourceBean implements Parcelable {
            private String id;
            private String name;

            public SourceBean() {
            }

            protected SourceBean(Parcel in) {
                id = in.readString();
                name = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(id);
                dest.writeString(name);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<SourceBean> CREATOR = new Creator<SourceBean>() {
                @Override
                public SourceBean createFromParcel(Parcel in) {
                    return new SourceBean(in);
                }

                @Override
                public SourceBean[] newArray(int size) {
                    return new SourceBean[size];
                }
            };

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return "SourceBean{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        '}';
            }
        }

        protected News(Parcel in) {
            author = in.readString();
            content = in.readString();
            description = in.readString();
            publishedAt = in.readString();
            source = in.readParcelable(SourceBean.class.getClassLoader());
            title = in.readString();
            url = in.readString();
            urlToImage = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(author);
            dest.writeString(content);
            dest.writeString(description);
            dest.writeString(publishedAt);
            dest.writeParcelable(source, flags);
            dest.writeString(title);
            dest.writeString(url);
            dest.writeString(urlToImage);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<News> CREATOR = new Creator<News>() {
            @Override
            public News createFromParcel(Parcel in) {
                return new News(in);
            }

            @Override
            public News[] newArray(int size) {
                return new News[size];
            }
        };

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public SourceBean getSource() {
            return source;
        }

        public void setSource(SourceBean source) {
            this.source = source;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrlToImage() {
            return urlToImage;
        }

        public void setUrlToImage(String urlToImage) {
            this.urlToImage = urlToImage;
        }

        @Override
        public String toString() {
            return "News{" +
                    "author='" + author + '\'' +
                    ", content='" + content + '\'' +
                    ", description='" + description + '\'' +
                    ", publishedAt='" + publishedAt + '\'' +
                    ", source=" + source +
                    ", title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    ", urlToImage='" + urlToImage + '\'' +
                    '}';
        }
    }

}
