package uz.pdp.eventorganizerbot.messages;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BotMessages {

    CHOOSE_MENU(
            "\uD83D\uDCCBPlease select from the menu below!",
            "\uD83D\uDCCBIltimos, quyidagi menyudan tanlang!",
            "\uD83D\uDCCBПожалуйста, выберите из меню ниже!"
    ),

    CREATE_EVENT(
            "\uD83D\uDECDCreate Event",
            "\uD83D\uDECDTadbir yaratish",
            "\uD83D\uDECDСоздать событие"
    ),

    MY_EVENTS(
            "\uD83D\uDCC5My Events",
            "\uD83D\uDCC5Mening tadbirlarrim",
            "\uD83D\uDCC5Мои события"
    ),

    INVITE_FRIENDS(
            "\uD83D\uDE4BInvite Friends",
            "\uD83D\uDE4BDo'stlarni taklif qilish",
            "\uD83D\uDE4BПригласить друзей"
    ),

    HELP(
            "❓Help",
            "❓Yordam",
            "❓Помощь"
    ),

    BACK(
            "⬅️Back",
            "⬅️Orqaga",
            "⬅️Назад"
    ),

    SKIP(
            "➡️Skip",
            "➡️O'tkazib yuborish",
            "➡️Пропустить"
    ),

    CONFIRM(
            "✅ Confirm",
            "✅ Tasdiqlash",
            "✅ Подтвердить"
    ),

    EDIT(
            "✏️ Edit",
            "✏️ Tahrirlash",
            "✏️ Редактировать"
    ),

    CANCEL(
            "❌ Cancel",
            "❌ Bekor qilish",
            "❌ Отменить"
    ),

    RSVP_YES(
            "✅ Yes, I’ll attend",
            "✅ Ha, qatnashaman",
            "✅ Да, я приму участие"
    ),

    RSVP_NO(
            "❌ No, I can’t make it",
            "❌ Yo‘q, bora olmayman",
            "❌ Нет, я не смогу"
    ),

    RSVP_MAYBE(
            "\uD83E\uDD14 Maybe",
            "\uD83E\uDD14 Balki",
            "\uD83E\uDD14 Может быть"
    ),

    EVENT_DETAILS(
            "🎉 Event Name: %s\n⏰ Date & Time: %s\n📍 Venue: %s\n📝 Description: %s\n👥 Max Participants: %s",
            "🎉 Tadbir nomi: %s\n⏰ Sana va vaqt: %s\n📍 Manzil: %s\n📝 Tavsif: %s\n👥 Maksimal qatnashchilar: %s",
            "🎉 Название мероприятия: %s\n⏰ Дата и время: %s\n📍 Место проведения: %s\n📝 Описание: %s\n👥 Макс. участников: %s"
    ),

    EVENT_DETAILS_V2(
            "Hi %s! You've been invited to an event:\n🎉 Event Name: %s\n⏰ Date & Time: %s\n📍 Venue: %s\n📝 Description: %s\n👥 Max Participants: %s\n🕒 Created On: %s",
            "Salom %s! Siz tadbirga taklif qilindingiz:\n🎉 Tadbir nomi: %s\n⏰ Sana va vaqt: %s\n📍 Manzil: %s\n📝 Tavsif: %s\n👥 Maksimal qatnashchilar: %s\n🕒 Yaratilgan vaqt: %s",
            "Привет, %s! Вы приглашены на мероприятие:\n🎉 Название мероприятия: %s\n⏰ Дата и время: %s\n📍 Место проведения: %s\n📝 Описание: %s\n👥 Макс. участников: %s\n🕒 Создано: %s"
    ),

    CREATE_EVENT_MESSAGE(
            "\uD83D\uDE4B Let's create your event! Please provide the following details step-by-step.",
            "\uD83D\uDE4B Keling, tadbiringizni yaratamiz! Iltimos, quyidagi ma'lumotlarni bosqichma-bosqich taqdim eting.",
            "\uD83D\uDE4B Создадим ваше событие! Пожалуйста, предоставьте следующие данные шаг за шагом."
    ),

    EVENT_NAME(
            "\uD83C\uDF89 Event Name:",
            "\uD83C\uDF89 Tadbir nomi:",
            "\uD83C\uDF89 Название мероприятия:"
    ),

    EVENT_DATE_TIME(
            "\uD83D\uDCC5 Date and Time:",
            "\uD83D\uDCC5 Sana va vaqt:",
            "\uD83D\uDCC5 Дата и время:"
    ),

    EVENT_VENUE(
            "\uD83C\uDFE0 Venue:",
            "\uD83C\uDFE0 Manzil:",
            "\uD83C\uDFE0 Место проведения:"
    ),

    EVENT_DESCRIPTION(
            "\uD83D\uDCDD Description (Optional):",
            "\uD83D\uDCDD Tavsif (ixtiyoriy):",
            "\uD83D\uDCDD Описание (по желанию):"
    ),

    EVENT_MAX_PARTICIPANTS(
            "\uD83C\uDFE2 Max Participants (Optional):",
            "\uD83C\uDFE2 Maksimal qatnashchilar (ixtiyoriy):",
            "\uD83C\uDFE2 Макс. участников (по желанию):"
    ),

    EVENT_CONFIRMATION(
            "✅ Event created successfully! 🎉 \nYou can invite friends to your event by sharing the link below:",
            "✅ Tadbir muvaffaqiyatli yaratildi! 🎉 \nDo'stlaringizni tadbirga taklif qilish uchun quyidagi havolani ulashing:",
            "✅ Мероприятие успешно создано! 🎉 \nВы можете пригласить друзей на мероприятие, поделившись ссылкой ниже:"
    ),

    HELP_MESSAGE(
            """
            Hello! 👋
            
            This bot is here to help you organize events effortlessly. Here’s how you can use it:
            
            1️⃣ Create an Event \s
               Use the "Create Event" option to set up an event. Provide details like the event name, date, and time.
            
            2️⃣ Invite Friends \s
               Once the event is created, you’ll get a unique invitation link to share with your friends.
            
            3️⃣ Track Responses \s
               See who has accepted, declined, or responded with "maybe" to your event invitations.
            
            4️⃣ Manage Events \s
               View and edit your active events, and stay updated on participant statuses.
            
            5️⃣ Notifications \s
               Get real-time notifications when someone responds to your event invitation.
            \s
            Enjoy organizing with Event Organizer Bot!
            """,
            """
            Salom! 👋
            
            Ushbu bot sizga tadbirlarni oson tashkil qilishda yordam beradi. Botdan foydalanish yo‘llari:
            
            1️⃣ Tadbir yarating \s
               "Tadbir yaratish" tugmasi orqali tadbir tashkil qiling. Tadbir nomi, sanasi va vaqtini kiriting.
            
            2️⃣ Do‘stlarni taklif qiling \s
               Tadbir yaratilgach, sizga do‘stlaringiz bilan ulashish uchun noyob taklifnoma havolasi beriladi.
            
            3️⃣ Javoblarni kuzatib boring \s
               Takliflarga kim qabul qilgan, rad etgan yoki "ehtimol" deb javob berganini ko‘ring.
            
            4️⃣ Tadbirlarni boshqaring \s
               Faol tadbirlaringizni ko‘ring va tahrir qiling, ishtirokchilarning holatini kuzatib boring.
            
            5️⃣ Xabarnomalar \s
               Kimdir sizning tadbir taklifingizga javob berganda real vaqtda xabarnoma oling.
            \s
            Event Organizer Bot bilan tashkil qilishdan zavqlaning!
            """,
            """
            Здравствуйте! 👋
            
            Этот бот поможет вам легко организовывать мероприятия. Вот как им пользоваться:
            
            1️⃣ Создайте мероприятие \s
               Используйте кнопку "Создать мероприятие", чтобы настроить его. Укажите название, дату и время мероприятия.
            
            2️⃣ Пригласите друзей \s
               После создания мероприятия вы получите уникальную ссылку-приглашение, чтобы поделиться с друзьями.
            
            3️⃣ Отслеживайте ответы \s
               Узнайте, кто принял, отклонил или ответил "возможно" на ваши приглашения.
            
            4️⃣ Управляйте мероприятиями \s
               Просматривайте и редактируйте свои активные мероприятия, следите за статусами участников.
            
            5️⃣ Уведомления \s
               Получайте уведомления в реальном времени, когда кто-то отвечает на ваше приглашение.
            \s
            Наслаждайтесь организацией с Event Organizer Bot!
            """
    ),


    NOTIFY_ORGANIZER(
            """
            Hello %s,
    
            Your friend %s has responded to your event: "%s"
            Response: %s
    
            Here are the updated event details:
            Date: %s
            Participants: %d accepted, %d declined, %d maybe
    
            Thank you for using Event Organizer Bot!
            """,
            """
            Salom %s,
    
            Do'stingiz %s "%s" tadbiriga javob berdi.
            Javob: %s
    
            Mana yangilangan tadbir tafsilotlari:
            Sana: %s
            Ishtirokchilar: %d qabul qildi, %d rad etdi, %d ehtimol
    
            Event Organizer Bot dan foydalanganingiz uchun rahmat!
            """,
            """
            Здравствуйте %s,
    
            Ваш друг %s ответил на ваше мероприятие: "%s".
            Ответ: %s
    
            Вот обновленные данные о мероприятии:
            Дата: %s
            Участники: %d приняли, %d отклонили, %d возможно
    
            Спасибо, что пользуетесь Event Organizer Bot!
            """
    );



    private final String en;
    private final String uz;
    private final String ru;

    public static String getSkipped(String languageCode) {
        if (languageCode.equals("uz")) {
            return "o'tkazib yuborilgan";
        } else if (languageCode.equals("ru")) {
            return "пропущен";
        } else {
            return "skipped";
        }
    }

    public String getMessage(String langCode) {
        return switch (langCode.toLowerCase()) {
            case "uz" -> uz;
            case "ru" -> ru;
            default -> en;
        };
    }

}
