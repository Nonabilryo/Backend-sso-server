package nonabili.ssoserver.util.error

class CustomError(val reason: ErrorState): RuntimeException(reason.message)