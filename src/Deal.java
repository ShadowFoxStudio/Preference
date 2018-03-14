import java.util.ArrayList;
import java.util.Arrays;

public class Deal {
    private String contract;  // контракт на игру
    private ArrayList<String> historyOfContracts;  // история заявок в раздаче
    private final ArrayList<String> tableOfContracts = new ArrayList<>(Arrays.asList( // таблица контрактов
         /* "\u2660",  //пики
            "\u2667",  //крести
            "\u2662",  //бубны
            "\u2661",  //червы
            "Б.К."     //без козыря
            мизер перебивается начиная с 9 пик */
            "6\u2660", "6\u2667", "6\u2662", "6\u2661", "6Б.К.",
            "7\u2660", "7\u2667", "7\u2662", "7\u2661", "7Б.К.",
            "8\u2660", "8\u2667", "8\u2662", "8\u2661", "8Б.К.",
            "Мизер",
            "9\u2660", "9\u2667", "9\u2662", "9\u2661", "9Б.К.",
            "10\u2660", "10\u2667", "10\u2662", "10\u2661", "10Б.К."));

    Deal(){
        contract = "";
        historyOfContracts = new ArrayList<>();
    }

    public ArrayList<String> getContractHistory() {
        return historyOfContracts;
    }

    public void setContract(Bot bot, boolean somebodyIsPassed, Game.Gamestat gamestat, String bid){
        // игрок спасовал
        if(bid.equals("")) {
            historyOfContracts.add(bot.getName() + ": Пас");
            bot.setBotStatus(false);
            bot.setWhistStatus(false);
            return;
        }
        //если заявок не было (первая заявка)
        if(contract == ""){
            bidIsOK(bot, bid);
            return;
        }
        int indexOfBid = tableOfContracts.indexOf(bid);
        int indexOfContract = tableOfContracts.indexOf(contract);
        //заявляет новый контракт (заказ мизера возможен только во время торгов)
        if(indexOfBid > indexOfContract){
            if(bid.equals("Мизер")){
                if(gamestat == Game.Gamestat.IS_DEAL){
                    bidIsOK(bot, bid);
                    bot.setWhistStatus(false);
                }
                //идет игра
                else{
                    return;
                }
            }
            else{
                bidIsOK(bot, bid);
                bot.setWhistStatus(true);
            }
        }
        else if(indexOfBid == indexOfContract){
            //перебивает эту же заявку, при условии что это не мизер и один из игроков спасовал
            if(!bid.equals("Мизер") & somebodyIsPassed){
                bidIsOK(bot, bid);
                bot.setBotStatus(true);
            }
            else {
                bot.setBotStatus(false); // нужно пасовать
                bot.setBotStatus(false);
                historyOfContracts.add(bot.getName() + ": Пас");
            }
        }
        //заявка меньше текущей заявки
        else {
            bot.setBotStatus(false); // нужно пасовать
            bot.setBotStatus(true);
            historyOfContracts.add(bot.getName() + ": Пас");
        }
    }

    private void bidIsOK(Bot bot, String bid){
        contract = bid;
        historyOfContracts.add(bot.getName() + ": " + bid);
        bot.setBotStatus(true);
    }
}
