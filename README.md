# Курсовая работа "Автоматизация приемного отделения больницы"

Курс "ООП", 4 семестр.

База данных включает 3 таблицы:
- Пациенты: id, ФИО, id палаты, id даигноза
- Палаты: id, название (уникальное)
- Диагнозы: id, название

Доступны 3 вида пользователей:
- Неавторизованный пользователь: разрешены только запросы на выборку данных
- user: открывается доступ ко всем операциям с диагнозами и пациентами (добавление, обновление, удаление)
- admin: открывается доступ ко всем операциям с палатами