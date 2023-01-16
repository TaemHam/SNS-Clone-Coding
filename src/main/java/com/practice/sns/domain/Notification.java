package com.practice.sns.domain;

import com.practice.sns.domain.constant.NotificationType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

@Getter
@Entity
@Table(name = "\"notification\"", indexes = {
        @Index(columnList = "user_id")
})
@NoArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@SQLDelete(sql = "UPDATE \"notification\" SET deleted_at = NOW() where id=?")
@Where(clause = "deleted_at is NULL")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Type(type = "jsonb")
    @Column(columnDefinition = "json")
    private NotificationArgs notificationArgs;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    private Notification(Long id, User user, NotificationType notificationType, NotificationArgs notificationArgs) {
        this.id = id;
        this.user = user;
        this.notificationType = notificationType;
        this.notificationArgs = notificationArgs;
    }

    public static Notification of(User user, NotificationType notificationType, NotificationArgs notificationArgs) {
        return new Notification(null, user, notificationType, notificationArgs);
    }

    public static Notification of(Long id, User user, NotificationType notificationType,
                                  NotificationArgs notificationArgs) {
        return new Notification(id, user, notificationType, notificationArgs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notification notification = (Notification) o;
        return Objects.equals(id, notification.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

