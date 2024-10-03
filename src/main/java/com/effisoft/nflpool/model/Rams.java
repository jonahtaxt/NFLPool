package com.effisoft.nflpool.model;

public class Rams extends Team {
    public Rams() {
        this.setPoolName("LA RAMS");
        this.setLogo("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAALfklEQVR42u2aD1TUVRbHp5wQkMhKd3W11hINk1pPkTsZEbuZuckama22UZFyNlexyLSDaWriLiaaCeGfRUP+mAYoGiLiCAhoKAiooyCiDoIyymjI35l5f3537xtByZzfDIJ/9uzMOd/D4Zz5vffu59133733NwoAUPw/S2EHYAdgB2AHYAdgB2AHYAdgB2AHYAdglo+Pz22Tn5+fQ3h4uCo9PX1qRUVFOKV0E64hA5WNUqNSdDrdyvz8/BmRkZEvT5w48f6umDcsLMwygFv96dWrl3NwcPA7eXl523C+JhScPldPdu2vouu2HWOLY4tMC9cWkH/HHDStTNbQlD2n6LHTlwjjkoRfpVqttnD58uWzPTw8HrnZNQQEBNx+AMOGDXs0JSUlymAw/NzQTHhcWhkdH5JOHx4ZTRXPRYI1uXivZq9O30YREEFgDNfKEOJmX1/fZ+9qALjjPWJiYr7CsY2HTujZ9PAc0wuByaa/fb6TfrQ01zR3Vb40O+onNiUsm/h+kkofez2WoMGSHIx7hkdKo4K20Yz9VQTHlbKzs79XqVRP3HUARo8e7anX6ytMhPP8I7qWnOJzpNlAOUiEg6GUQGMWg4a0FmhIN0BTNgPjCQoSk2r0TeI4UL+ZaVT5fJQsjJc+3EIRLIYPasB4MkOpVN5zxwHgIhR4TmeJRUHbp+k4hVPzKJS8SOGn7hwKcLpi1OHrpHFmcOpPFGqXEjBVsKrzjSwoPIc4jLAMQqmKkj4My6ZNCBcD5tY+ffrcf0cBoPEfCdc0G96sZaD5BwW1E4dMnGIPah/KEoBfwOjB4exUCqSKZRVW00Hj4pmcN4yctpVeqjcICDsQgvMdAYDGB5kNpw0cSucR2I67nYZDZ6A6CuAqCIR3IZQ0NbfwWRH7ZGPEE+Pj2RldAysrK8tyc3PrdVsBTJkyZRyOweHyYQY7BzJIwSF/RHUWQJsqnqdgLGUxqaWkm+pbixAGvB7Lqi80coSwH4Ow020B4OXlNQTPfCNcyGSwxYVDMg7X1QCEjj7IoXkfS0gvp/cOtwxh6IQNZghqtTpBxKQuBbBx48ZIvHrS2guj/RmozaOQ5CxBIg5lOwAJjTKAdqwBLoQZ4XIy3g7ZHJr2MKhPpaCPoFAVQKG0H7sGwRVvkUNs0XeFRC4mDA9IpIRyaebMme93GQC8b92Em+/YV2nckn3qmrIqjFuW+TVvCf2DcdcSd6MNADgc/Z0RapcYgJ6nImw0NjZWYQDLQKAJuHPxmOj8iGBLzceKN/Ck2HeMBxJdqRlC+dOUmBr5c+//IJtIfbPxEKmrqzvbv39/1y4BgLsfUVKul5304ylvUosAcnDHC5QmqJljBN7MMdc/tmDBgmmenp6P32g+R0fHblgn7Ms/qKGug16WejzuA+/4DyUXchwYnA0SaTJzkMkVeniv5mfONzBMxsI7DQB3f7A45+/NV1t0vW7DV0inox9mNwSQo8Dd622CplyKKfGlkJCQD9DAe+XmxCJobn1DE3/SeyITxrdp4LMjeGFyTwothTT461zZo+A/b5fwMAOm4/06BSApKSmqRt/MHEastEh83N8D6Q1jQK6CQNkgcxTHgqYYi5lHrXkb5vkqcaFOCl5E2hvfpt8OfVEq2PmW4fylZt5n9DqLOcK9f/xWOl5ZR9ELltw0gAEDBjyIu980Z1W+LO29yx7/NYAcNP5oTy4yO3TnIkxSHrBmPALqj+f/5IbtRabuQwK44xMTifPAUex6CI898wI7rztF5q3ZL3sspy3ZI7zuoru7e5+bAoDn9J8iy3rEN8Yi6SGvzqXSxnvgFwB2odsfwnOv/5pikDuH9fgQW1JpDISJ+roW3vcv3/GrRZDnMqZ8eiZ1GjT2KoghL05k4ohgXJLdGOEhjEnS3LlzAzsMQJxTDFbHF68/KEt5dcgLBDYprgH4UcGgBKN9+VBzkRMYGOhryzWLsSFAXF9jZ2y3MF+E5OAxlWBQ5D8VHhGF0CVxizz61/Wy68s6WE3xltneYQD4wBixoH5jLO9+L+/FrCXuPn4VwGbc9Xx0fXFl1SXQkpKSLFuMx9vATQTaz66kvLI9gvlRaiN+t378+PGe+PdnDHayz+CYAlZjUFCQskMAcPG7N+wsl6U7Z5ovge9xiDYA6lbjj7oykAwc02Q/a8a7uLjchzHiQCYWPiJwyc2nmpREKeMSesu7rWvMXJpQLLvGEZOTRf8A4uPjh9kMYOTIkU+Lhzzfs5xw9PYOYzX/QUOvAZCgSHElc6t8Q1xBzZiTO1oDEB0d/S8RZ/rLeJqQq88afupsPRONkLY0F/OTNcmZJ2U9wOWl1UxfZ2AI612bAaSmpsbklZyTJTsv6DUCG/DxNgA7FPRq2nphEdFoNHnWjEc39hZtrnGf7bDq+nFpx0lrl8Eo4LaK7ik6y6w9m6/RUcwMv7AJAKaPD+CZaQj4crd84hOFiU97ADntAPy8niLEBDnj8YrtXV1drdmWe5paa4d1H7GSTf1qjxGvtasKWpzR8sn8b4wTpi9vsgYgNU8rPHKFTQDCwsI+1V1s5jgptzTgm29PppCAj7YHcLAdgMtJRszp0zCQ+lpSYWHhjhp9E+/9ylpuS4P0BrcCc3Z7jToOftPq8/E7jpvQznVWAeDu90RXqfxi9X5Zl8wMG2w0xiqZMQ4Vj9qkNBqL8G+bahMMRsKYJXFJkgwmZm5y3pzxV+Q0eJzJ0f1tW49PjFUA4eHhs8TCeo9ayzqzMDm5v5XARJP0o6W5pLNjOQ32Iw5PBlqFuDnrpPCAlbIA8DpS4u5XRW89Rm6V8aKeKD5ey7CspqLN3ZmxMEOkzgNfMWeJ1r6bV1IjMtKFsgDwXL4mEh/VB0m3DMCn3+wl4gWJ2xtxsh7m5LWKY55PvowuoJYUuibHFBYRS52eXya7XqUqimlr6klZWdmHsgDwnty1MeMEvVXGi06NiTDpg4W7rQKOTDxMcMcuZct8sMipP3LyotX1ekzYQMULtpSUFG+LADDxeUq0tMUib4Hb87dC0s2t60R1hdXxx3ySSnG93N/ff5TMFfowXtUts1bstQpzcmimuAIZFkT3WwSAdNYJStqaBm6TqvVMu3AQ1c5+iJv1+UNE+y3+jfqlKouSjRjwWNt7ElHDWxsbvy9hhrfMSht+thgPr2vJ2niXG02SVqs9IFsMYdnrjwq0VaJmh4IICgvx8TatV7RgXmCCeFQc/i+U3J+BoZaLogVvmKk2jj+pZ8+e3eUAYGEztiPrxYzTq0vb4liMvA/UIEHcn69AiMAiKEvRDMVo/EHUAUyK8lAZqAOvG4A2cpFei8JHcYc+XQpAvIzETC4NGs5xWNvHZM4Et7fLBH/9coMBreVY9e338vJy/58H0BqIeqFBBXCxgEGyCzdXg4UyEEofYdC4WwQkgjFnjY+Pj8f1Ly9s/aBbd1g4Z9e/GRK9Np1OdwL0exlsfZDDThkAh1tfiFSOM3d1RRATaTcuLBbrDzxVIZODg4MnhYaGzkhKSlqF4x7Br9RHRkaGXA+qy38j1JkPQuiHi9VAQzkD9VMU9rY2RaypHL977mMKdT8gkGIKpkoG5AwFg4ZC/TYKujmoEFH6MpVK5XHXAhAf0fHF6m8rMAyMp5cZ4bAr7fD7v18dmb4MLm9u6+r2vasBtNYSDtHR0aGilwCGowzOTCBwRCl12HCNc+vvA6oZZoO6wMDAMdfPdVcCaHck+oofRolMDWgNA/0Kam6RHfsNsRgXyn5PoMofj8MmAuwyFz+qwrO/AD3L9UZz3NUA2qerGNCmazSa7Nb2FZ7mi3jGDxFoykXtNaGnUGD15oCIwOrUanWieKuLhveQG7vLAdzMtdIRYerqgDHiGcwg38b5PkXNR4keXTAG0PFo+FDMFLvZOp79p7J2AHYAdgB2AHYAdgB2AHYAdgA3p/8CnoqtN3iAQxAAAAAASUVORK5CYII=");
    }
}