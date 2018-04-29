/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mai.textanalyzer.dao.topic;

/**
 *
 * @author Sergey
 */
public class TopicService implements ITopicService {

    private ITopicDao topicDao;

    public void setTopicDao(ITopicDao topicDao) {
        this.topicDao = topicDao;
    }

    @Override
    public Topic getOrInserteAndGetTopicByName(String name) {
        Topic topic = topicDao.getTopicByName(name);
        if (topic != null) {
            return topic;
        }
        return topicDao.getTopicByID(topicDao.insertTopic(name));
    }

    @Override
    public Topic getTopicByName(String name) {
        return topicDao.getTopicByName(name);
    }

    @Override
    public Topic getTopicByID(int id) {
        return topicDao.getTopicByID(id);
    }

}
