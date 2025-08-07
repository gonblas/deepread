const BASE_URL = 'https://tu-api.com/api';

interface CustomFetchOptions<T = unknown> {
  body?: T;
  headers?: HeadersInit;
  method?: string;
  credentials?: RequestCredentials;
  cache?: RequestCache;
  redirect?: RequestRedirect;
  referrer?: string;
  referrerPolicy?: ReferrerPolicy;
  integrity?: string;
  keepalive?: boolean;
  signal?: AbortSignal | null;
  mode?: RequestMode;
}

export async function customFetch<TResponse = any, TRequest = unknown>(
  endpoint: string,
  options: CustomFetchOptions<TRequest> = {}
): Promise<TResponse> {
  const { body, headers, ...rest } = options;

  const config: RequestInit = {
    ...rest,
    headers: {
      'Content-Type': 'application/json',
      ...headers,
    },
    body: body ? JSON.stringify(body) : undefined,
  };

  try {
    const response = await fetch(`${BASE_URL}${endpoint}`, config);

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.message || 'Error en la solicitud');
    }

    const data: TResponse = await response.json().catch(() => ({} as TResponse));
    return data;
  } catch (error: any) {
    throw new Error(error.message || 'Error desconocido');
  }
}
