package org.example.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.myblog.domain.entity.Message;
import org.example.myblog.domain.vo.MessageVO;

import java.util.List;

/**
 * 留言墙 Service 接口
 */
public interface IMessageService extends IService<Message> {

    /**
     * 查询留言列表（按创建时间倒序，取最近若干条）
     * @return 留言列表（VO，屏蔽 updateTime 等内部字段）
     */
    List<MessageVO> queryMessageList();

    /**
     * 保存留言
     * @param message 留言对象（昵称、内容）
     * @return 插入后的展示对象（含主键 id 与自动填充的 createTime）
     */
    MessageVO saveMessage(Message message);
}
