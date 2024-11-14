#!/bin/bash

# Переменные
BACKUP_DIR=/backups
LOG_DIR=/backup-logs
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE=$BACKUP_DIR/mentorlik_db_backup_$DATE.sql
LOG_FILE=$LOG_DIR/backup_$DATE.log
DB_NAME=${POSTGRES_DB}
DB_USER=${POSTGRES_USER}
DB_PASSWORD=${POSTGRES_PASSWORD}
HOST=db

# Устанавливаем переменную окружения для пароля
export PGPASSWORD=$DB_PASSWORD

# Создаем папки для резервных копий и логов, если их еще нет
mkdir -p $BACKUP_DIR
mkdir -p $LOG_DIR

{
    # Начало логирования
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] Starting backup for database: $DB_NAME"

    # Создаем резервную копию
    if pg_dump -h $HOST -U $DB_USER $DB_NAME > $BACKUP_FILE; then
        echo "[$(date '+%Y-%m-%d %H:%M:%S')] Backup successful: $BACKUP_FILE"
    else
        echo "[$(date '+%Y-%m-%d %H:%M:%S')] Backup failed!"
        exit 1
    fi

    # Очистка старых резервных копий (оставляем последние 7 файлов)
    find $BACKUP_DIR -type f -name "*.sql" -mtime +7 -exec rm {} \;
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] Old backups cleaned up."

    # Логирование завершено
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] Backup process completed."

} >> $LOG_FILE 2>&1