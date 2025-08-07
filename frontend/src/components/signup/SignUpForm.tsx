import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useAuth } from "@/contexts/authContext";
import { useForm } from "@/hooks/useForm";
import FormField from "@/components/form/FormField";

export function SignUpForm({
  className,
  ...props
}: React.ComponentProps<"div">) {
  const { signup } = useAuth();

  const { values, handleChange, errors, setErrors, resetForm } = useForm({
    email: "",
    password: "",
    username: "",
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const { email, password, username } = values;
    const newErrors: { email?: string; password?: string; username?: string } =
      {};

    if (!email) {
      newErrors.email = "Email is required";
    } else if (!/\S+@\S+\.\S+/.test(email)) {
      newErrors.email = "Email address is invalid";
    }

    if (!password) {
      newErrors.password = "Password is required";
    } else if (password.length < 8 || password.length > 64) {
      newErrors.password = "Password must be between 8 and 64 characters";
    }

    if (!username) {
      newErrors.username = "Username is required";
    } else if (username.length < 8 || username.length > 64) {
      newErrors.username = "Username must be between 8 and 64 characters";
    }

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    setErrors({});
    try {
      await signup(email, password, username);
      resetForm();
    } catch (error) {
      console.error("Login failed:", error);
    }
  };

  return (
    <div className={cn("flex flex-col gap-6", className)} {...props}>
      <form className="grid gap-6">
        <div className="flex flex-col items-center gap-2 text-center">
          <h1 className="text-2xl font-bold">Create an account</h1>
          <p className="text-muted-foreground text-sm text-balance">
            Enter your details below to create your account.
          </p>
        </div>
        <div className="grid gap-4">
          <FormField
            label="Email"
            id="email"
            name="email"
            type="email"
            placeholder="email@example.com"
            value={values.email}
            onChange={handleChange}
            error={errors.email}
          />
          <FormField
            label="Password"
            id="password"
            name="password"
            type="password"
            value={values.password}
            onChange={handleChange}
            error={errors.password}
            labelSuffix={
              <a
                href="#"
                className="ml-auto text-sm underline-offset-4 hover:underline"
              >
                Forgot your password?
              </a>
            }
          />
          <FormField
            label="Username"
            id="username"
            name="username"
            type="username"
            placeholder="Your username"
            value={values.username}
            onChange={handleChange}
            error={errors.username}
          />
          <Button type="submit" className="w-full" onClick={handleSubmit}>
            Sign Up
          </Button>
        </div>
        <div className="text-muted-foreground *:[a]:hover:text-primary text-center text-xs text-balance *:[a]:underline *:[a]:underline-offset-4">
          By clicking continue, you agree to our{" "}
          <a href="#">Terms of Service</a> and <a href="#">Privacy Policy</a>.
        </div>
      </form>
    </div>
  );
}
