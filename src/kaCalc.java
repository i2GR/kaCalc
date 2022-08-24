import java.util.Scanner;

public class kaCalc {
    public static void main(String[] args) {
        String calculationResult;
        Main Calculation = new Main();								// как абитуриенту пока непонятно почему без этой конструкции наличие calc() в методе main вызывает ошибку non-static method calc(java.lang.String) cannot be referenced from a static context
        System.out.println("Hello calc!");
        System.out.println("Enter Expression to calculate");
        System.out.println("Use pair of arabic numbers 1...10 (!only) or pair of roman numbers I...X (!only) and one arithmetic expression '+' '-' '*' '/'");
        Scanner InputAsExpression = new Scanner(System.in);
        while(true){ // цикл для удобства проверки
            String initialExpression = InputAsExpression.nextLine();
            calculationResult = Calculation.calc(initialExpression); //  метод calc принимает строку и возвращает строку
            if(calculationResult.startsWith("throws Exception")){
                System.out.print("Exception. Bad Input. Exit");
                break;
            }
            System.out.println(calculationResult);
            System.out.print("Press 'E' to exit, press anykey to restart");
            initialExpression = InputAsExpression.nextLine();
            if(initialExpression.equals("E")) {
                break;
            } else {
                System.out.println("New calculation: Use pair of arabic numbers 1...10 (!only) or pair of roman numbers I...X (!only) and arithmetic expressions '+' '-' '*' '/'");
            }
        }
    }

}
class Main {





    char[] operationsAllowed = {'+', '-', '*', '/'}; // список допустимых операций
    String[] romanUnits = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"}; // список римских значений "единиц" 0 - 10. X (рим.10) используется только для проверки правильности ввода
    String[] romanTens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC", "C"}; // список римских значений "десятков" 10 - 100
    int operatorIndex = -1;
    int determineOperatorIndex(String input) { // ф. для определения позиции символа допустимой операции и проверки
        int index = -1;                        // нач. значение
        int operatorCount = 0;                 // переменная, указывающая о количестве символов в строке больше 1
        for (int i = 0; i < 4; i++) { // поиск позиции
            if (input.indexOf(operationsAllowed[i]) > 0) {
                index = input.indexOf(operationsAllowed[i]);
                operatorCount += 1;
                if(input.lastIndexOf(operationsAllowed[i]) != index){ // проверка, что символ операции один в строке
                    operatorCount += 1;
                }
            }
        }
        if (operatorCount > 1){
            index = -2;
        }
        return index;
    }
    Operand newOperand(String str){ // функция возвращающая объект - операнд (см. ниже)
        int value;
        try {                       // самый простой способ через Integer.parseInt, хоть и через Exception
            value = Integer.parseInt(str);
            if (value >= 1 && value <= 10) {
                return new Operand(value, Type.ARABIC);
            }
        } catch (NumberFormatException nfe) {
            for (int i = 1; i < romanUnits.length; i++) {
                if (str.equals(romanUnits[i])) {
                    value = i;
                    return new Operand(value, Type.ROMAN);
                }
            }
        }
        return new Operand();
    }
    String formatToRoman(int value){
        int unit = value % 10;
        int ten = value / 10;
        return (romanTens[ten] + romanUnits[unit]);
    }
    String calc(String input) {
        String result = "throws Exception";
        /* как абитуриенту:) не очень понятен момент с выбросом исключений.
        Если обрабатывать исключения указанные в ТЗ, можно сразу предусмотреть функционал сообщения об ошибке
        без использования механизма throw/throws
        */
        operatorIndex = determineOperatorIndex(input); // определение позиции символа операции
        if (operatorIndex < 0){ // если символ не найден возвращается результат "throws Exception"
            return result;
        }
        //char operator = input.charAt(operatorIndex);
        //System.out.println("operator "+ operator);
        Operand operand1 = newOperand(input.substring(0, operatorIndex).trim()); // "конвертация" подстроки до символа операции в первый операнд
        //System.out.println("operand1 " + operand1.value + ":" + operand1.type);
        Operand operand2 = newOperand(input.substring(operatorIndex + 1).trim()); // "конвертация" подстроки после символа операции во второй операнд
        //System.out.println("operand2 " + operand2.value + ":" + operand2.type);
        if (operand1.type == Type.UNDEF || operand2.type == Type.UNDEF){ // необязательный условный блок
            return result;
        }
        if(operand1.type == operand2.type) {
            int numericResult;
            switch (input.charAt(operatorIndex)) {
                case '+':
                    numericResult = operand1.value + operand2.value;
                    break;
                case '-':
                    numericResult = operand1.value - operand2.value;
                    break;
                case '*':
                    numericResult = operand1.value * operand2.value;
                    break;
                default:
                    numericResult = operand1.value / operand2.value;
                    break;
            }
            if (operand1.type == Type.ROMAN){ // отдельная проверка для римских чисел
                if (numericResult < 1){
                    return result;
                } else {
                    result = formatToRoman(numericResult);
                }
            } else if (operand1.type == Type.ARABIC){
                result = "" + numericResult;
            }
        }
        return result;
    }
    enum Type{ // объект для хранения типа числа римское/арабское
        ROMAN,
        ARABIC,
        UNDEF // тип числа не определен. для выброса ошибки
    }
    class Operand{ // класс для хранения данных об операнде: целочисленное значение и тип числа: римское/арабское
        int value;
        Type type;
        Operand(int value, Type type){ // конструктор, если операнд можно определить как арабское или римское число
            this.value = value;
            this.type = type;
        }
        Operand(){ // конструктор, если операнд нельзя определить как арабское или римское число
            this.type = Type.UNDEF;
        }
    }
}
