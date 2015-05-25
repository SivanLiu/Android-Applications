package com.example.downloadDB;

import java.util.List;

import com.example.entities.ThreadInfo;

/*
 * ���ݷ��ʽӿ�
 * */
public interface ThreadDAO {
	/*
	 * �����߳���Ϣ
	 */
	public void insertThread(ThreadInfo threadInfo);
	/*
	 * ɾ���߳�
	 * @param url
	 * @param thread_id
	 * */
	public void deleteThread(String url,ThreadInfo treahd_id);
	/*
	 * �����߳����ؽ���
	 * @param url
	 * @param thread_id
	 * @param finished 
	 * */
	public void updateThread(String url,int thread_id,int finshed);
	/*
	 * ��ѯ�ļ����߳���Ϣ
	 * @param url
	 * @return
	 * */
	public List<ThreadInfo> getThreads(String url);
	/*
	 * �߳���Ϣ�Ƿ��զ
	 * @param url
	 * @param thread_id
	 * @return
	 * */
	public boolean isExists(String url,int thread_id);
}
