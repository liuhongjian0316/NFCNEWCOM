package work.aijiu.nfcactuator.enum

object CommonEnum {
    enum class Product {
        U, R, E,ZXC, // 单元阀 户用阀 电动调节阀 直行程
    }

    enum class BottomPop {
        DEFAULT,
        LOCAL_CTL,
        COM_MODE,
        RS485,
        PARITY,
        BAUD_RATE,
        SELF_TEST_MODE,
    }


}