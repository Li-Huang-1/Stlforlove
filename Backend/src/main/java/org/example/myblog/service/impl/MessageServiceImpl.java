package org.example.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.myblog.domain.entity.Message;
import org.example.myblog.domain.vo.MessageVO;
import org.example.myblog.mapper.MessageMapper;
import org.example.myblog.service.IMessageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 留言墙 Service 实现
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

    /** 列表最多返回条数，避免全表扫描 */
    private static final int MAX_LIST_SIZE = 200;

    @Autowired
    private MessageMapper mapper;

    @Override
    public List<MessageVO> queryMessageList() {
        // 构造查询条件：按创建时间倒序，取最近 MAX_LIST_SIZE 条
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<Message>()
                .orderByDesc(Message::getCreateTime)
                .last("LIMIT " + MAX_LIST_SIZE);

        // 执行查询
        List<Message> messages = mapper.selectList(queryWrapper);

        // Entity -> VO（for 循环，屏蔽 updateTime 等内部字段）
        List<MessageVO> voList = new ArrayList<>();
        for (Message message : messages) {
            MessageVO vo = new MessageVO();
            BeanUtils.copyProperties(message, vo);
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public MessageVO saveMessage(Message message) {

        // 去除首尾空白后写回
        message.setNickname(message.getNickname().trim());
        message.setContent(message.getContent().trim());

        // 执行插入：mapper.insert 会回填自增主键 id，FieldHandler 自动填充 createTime/updateTime
        mapper.insert(message);

        // Entity -> VO（屏蔽 updateTime，对外只暴露展示字段）
        MessageVO vo = new MessageVO();
        BeanUtils.copyProperties(message, vo);
        return vo;
    }
}
