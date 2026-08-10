export class UserNotFoundError extends Error {
    statusCode = 404;

    constructor(message: string) {
        super(message);
        this.name = 'UserNotFoundError';
    }
}

export class DuplicateUserError extends Error {
    statusCode = 409;

    constructor(message: string) {
        super(message);
        this.name = 'DuplicateUserError';
    }
}

export class InvalidCredentialsError extends Error {
    statusCode = 401;

    constructor(message: string) {
        super(message);
        this.name = 'InvalidCredentialsError';
    }
}

export class SiblingNotFoundError extends Error {
    statusCode = 404;

    constructor(message: string) {
        super(message);
        this.name = 'SiblingNotFoundError';
    }
}

export class SiblingServiceUnavailableError extends Error {
    statusCode = 503;

    constructor(message: string) {
        super(message);
        this.name = 'SiblingServiceUnavailableError';
    }
}
