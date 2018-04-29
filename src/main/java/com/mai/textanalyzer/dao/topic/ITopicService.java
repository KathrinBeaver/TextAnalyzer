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
public interface ITopicService {

    Topic getOrInserteAndGetTopicByName(String name);

    Topic getTopicByName(String name);

    Topic getTopicByID(int id);

}
