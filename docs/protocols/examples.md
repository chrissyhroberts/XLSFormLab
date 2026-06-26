# Protocol Examples

## Calibrated scale: single value

```text
intent:#Intent;
action=com.example.xlsformlab.RUN_MODULE;
package=com.example.xlsformlab;
S.module_id=calibrated_scale;
S.return_mode=fields;
S.question=Rate your pain;
f.minimum=0;
f.maximum=100;
S.orientation=horizontal;
B.show_numbers=true;
end
```

## Calibrated scale: range

```text
intent:#Intent;
action=com.example.xlsformlab.RUN_MODULE;
package=com.example.xlsformlab;
S.module_id=calibrated_scale;
S.return_mode=json;
S.mode=range;
S.least_label=Least pain;
S.most_label=Worst pain;
f.minimum=0;
f.maximum=100;
end
```

## NFC read

```text
intent:#Intent;
action=com.example.xlsformlab.RUN_MODULE;
package=com.example.xlsformlab;
S.module_id=nfc_tags;
S.action_id=read;
S.return_mode=json;
end
```
