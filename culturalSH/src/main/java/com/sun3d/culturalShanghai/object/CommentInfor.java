package com.sun3d.culturalShanghai.object;

public class CommentInfor {
	private String commentId;
	private String commentRkName;
	private String commentRemark;
	private String commentTime;
	private String commentImgUrl;
	private String userHeadImgUrl;
	private float commentStar;
	private String commentUserSex;

	public String getCommentUserSex() {
		return commentUserSex;
	}

	public void setCommentUserSex(String commentUserSex) {
		this.commentUserSex = commentUserSex;
	}

	public String getCommentImgUrl() {
		return commentImgUrl;
	}

	public void setCommentImgUrl(String commentImgUrl) {
		this.commentImgUrl = commentImgUrl;
	}

	public String getUserHeadImgUrl() {
		return userHeadImgUrl;
	}

	public void setUserHeadImgUrl(String userHeadImgUrl) {
		this.userHeadImgUrl = userHeadImgUrl;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public float getCommentStar() {
		return commentStar;
	}

	public void setCommentStar(float commentStar) {
		this.commentStar = commentStar;
	}

	public String getCommentRkName() {
		return commentRkName;
	}

	public void setCommentRkName(String commentRkName) {
		this.commentRkName = commentRkName;
	}

	public String getCommentRemark() {
		return commentRemark;
	}

	public void setCommentRemark(String commentRemark) {
		this.commentRemark = commentRemark;
	}

	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	@Override
	public String toString() {
		return "CommentInfor [commentId=" + commentId + ", commentRkName=" + commentRkName + ", commentRemark=" + commentRemark + ", commentTime="
				+ commentTime + "]";
	}

}
