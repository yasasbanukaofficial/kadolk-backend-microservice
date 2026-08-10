const CONFIG_SERVER_URL = process.env.CONFIG_SERVER_URL ?? 'http://localhost:8888';

export async function loadRemoteConfig(): Promise<void> {
    try {
        const response = await fetch(`${CONFIG_SERVER_URL}/user-service/default`);
        if (!response.ok) {
            throw new Error(`config server responded with status ${response.status}`);
        }
        const body = (await response.json()) as {
            propertySources?: { source?: Record<string, string> }[];
        };
        const source = body.propertySources?.[0]?.source ?? {};
        for (const [key, value] of Object.entries(source)) {
            if (process.env[key] === undefined) {
                process.env[key] = String(value);
            }
        }
        console.log('Remote configuration loaded from config server');
    } catch (error) {
        console.warn('Config server unavailable, using local env:', (error as Error).message);
    }
}