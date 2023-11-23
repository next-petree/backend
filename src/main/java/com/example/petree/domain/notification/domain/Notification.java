package com.example.petree.domain.notification.domain;

import com.example.petree.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName    : com.example.petree.domain.notification.domain
 * fileName       : Notification
 * author         : 정세창
 * date           : 2023/07/16
 * description    : 알림 엔티티 클래스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/07/16         정세창              init
 */
@Entity
@Table(name = "notification")
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(name ="native", strategy = "native")
    private Long id;
    @Column
    private String content;
    @Column
    private LocalDateTime registrationTime;
    @Column
    private Boolean deleteFlag;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    public Notification(String content, LocalDateTime registrationTime, Member member) {
        this.content = content;
        this.registrationTime = registrationTime;
        this.deleteFlag = false;
        this.member = member;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
