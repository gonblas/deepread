import { Button } from "@/components/ui/button";

interface OAuthProps {
  name: string;
  logo: React.ReactNode;
}

export default function OAuthLoginButton({ name, logo }: OAuthProps) {
  return (
    <Button variant="outline" className="w-full flex items-center justify-center gap-2">
      <span>{logo}</span>
      <span className="text-sm font-medium">Login with {name}</span>
    </Button>
  );
}
