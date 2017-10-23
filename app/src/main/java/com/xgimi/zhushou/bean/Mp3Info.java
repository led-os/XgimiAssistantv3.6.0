package com.xgimi.zhushou.bean;

/**
 * 音乐文件java类
 * 
 */
public class Mp3Info {
	public static final int TYPE_CHECKED = 1;
	public static final int TYPE_NOCHECKED = 0;
	public int type;
	private long id;
	private String title;
	private String album;
	private long albumId;
	private String displayName;
	private String artist;
	private long duration;
	private long size;
	private String url;
	private String lrcTitle;
	private String lrcSize;
	private String time;

	public Mp3Info(){

	}
	public Mp3Info(long id,String title,String artist) {
		super();
		this.id=id;
		this.title=title;
		this.artist=artist;
	}

	public Mp3Info(long id, String title, String album, long albumId, String displayName, String artist, long duration, long size, String url,
			String lrcTitle, String lrcSize) {
		super();
		this.id = id;
		this.title = title;
		this.album = album;
		this.albumId = albumId;
		this.displayName = displayName;
		this.artist = artist;
		this.duration = duration;
		this.size = size;
		this.url = url;
		this.lrcTitle = lrcTitle;
		this.lrcSize = lrcSize;
	}

	@Override
	public String toString() {
		return "Mp3Info [id=" + id + ", title=" + title + ", album=" + album + ", albumId=" + albumId + ", displayName=" + displayName + ", artist="
				+ artist + ", duration=" + duration + ", size=" + size + ", url=" + url + ", lrcTitle=" + lrcTitle + ", lrcSize=" + lrcSize + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setType(int type){
		this.type=type;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(long albumId) {
		this.albumId = albumId;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	public void setFileTime(String time){
		this.time=time;
	}
	public String getFileTime(){
		return time;
	}

	public String getLrcTitle() {
		return lrcTitle;
	}

	public void setLrcTitle(String lrcTitle) {
		this.lrcTitle = lrcTitle;
	}

	public String getLrcSize() {
		return lrcSize;
	}

	public void setLrcSize(String lrcSize) {
		this.lrcSize = lrcSize;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}