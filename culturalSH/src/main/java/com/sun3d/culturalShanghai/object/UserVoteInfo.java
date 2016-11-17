package com.sun3d.culturalShanghai.object;

public class UserVoteInfo {
	private String voteContent;
	private String voteCount;
	private String voteCoverImgUrl;
	private String voteAddress;
	private int voteNum;

	public String getVoteContent() {
		return voteContent;
	}

	public void setVoteContent(String voteContent) {
		this.voteContent = voteContent;
	}

	public String getVoteAddress() {
		return voteAddress;
	}

	public void setVoteAddress(String voteAddress) {
		this.voteAddress = voteAddress;
	}

	public String getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(String voteCount) {
		this.voteCount = voteCount;
	}

	public String getVoteCoverImgUrl() {
		return voteCoverImgUrl;
	}

	public void setVoteCoverImgUrl(String voteCoverImgUrl) {
		this.voteCoverImgUrl = voteCoverImgUrl;
	}

	public int getVoteNum() {
		return voteNum;
	}

	public void setVoteNum(int voteNum) {
		this.voteNum = voteNum;
	}

}
