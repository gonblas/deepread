import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";

interface TextAreaFieldProps extends React.TextareaHTMLAttributes<HTMLTextAreaElement> {
  label: string;
  id: string;
  error?: string;
  labelSuffix?: React.ReactNode;
}

export default function TextAreaField({
  label,
  id,
  error,
  labelSuffix,
  ...props
}: TextAreaFieldProps) {
  return (
    <div className="form-field">
      <div className="label-field">
        <Label htmlFor={id}>
          {label} {props.required && <span className="text-red-500">*</span>}
        </Label>
        {labelSuffix}
      </div>
      <Textarea id={id} {...props} />
      <div className="h-4">
        {error && <p className="text-xs text-red-500">{error}</p>}
      </div>
    </div>
  );
}
