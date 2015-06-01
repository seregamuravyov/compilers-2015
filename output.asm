extern printf
extern scanf

extern strcmp
extern strcat
extern strcpy


extern malloc
extern memcpy
extern free


struc map
.index RESD 1
.value RESD 1
.size:
endstruc



struc vecmap
.x1 RESB map.size
.x2 RESB map.size
.size:
endstruc



struc firstClass
.num RESD 1
.flag RESD 1
.size:
endstruc



struc point
.x RESD 1
.y RESD 1
.size:
endstruc



struc vector
.a RESB point.size
.b RESB point.size
.size:
endstruc



struc dvector
.v1 RESB vector.size
.v2 RESB vector.size
.size:
endstruc



section .text
global main

glob_assign:
push ebp
mov ebp, esp
pusha
push 8
call malloc
add esp, 4
mov [motherfucker], eax
popa
push dword [globBoolVar]
pop eax
mov ecx, [motherfucker]
mov [ecx + firstClass.flag], eax
push dword [globIntVar]
pop eax
mov ecx, [motherfucker]
mov [ecx + firstClass.num], eax
mov esp, ebp
pop ebp
ret


doNothing:
push ebp
mov ebp, esp
sub esp, 8
mov ecx, [ebp + 8]
push dword [ecx + firstClass.num]
push int_format
call printf
add esp, 8
pusha
push 8
call malloc
add esp, 4
mov [ebp - 4], eax
popa
push dword [ebp + 8]
pop eax
mov ecx, [ebp - 4]
mov edx, [eax  + firstClass.num]
mov [ecx  + firstClass.num], edx
mov ecx, [ebp - 4]
mov edx, [eax  + firstClass.flag]
mov [ecx  + firstClass.flag], edx
push 1488
pop eax
mov ecx, [ebp - 4]
mov [ecx + firstClass.num], eax
mov ecx, [ebp + 8]
push dword [ecx + firstClass.num]
push int_format
call printf
add esp, 8
mov ecx, [ebp - 4]
push dword [ecx + firstClass.num]
push int_format
call printf
add esp, 8
pusha
push dword [ebp - 4]
call free
add esp, 4
popa
mov esp, ebp
pop ebp
ret 4



recurse:
push ebp
mov ebp, esp
sub esp, 4
push dword [ebp + 8]
push 20
pop ebx
pop edx
cmp edx, ebx
jb  L75
mov eax, 0
jmp L76
L75:
mov eax, 1
L76:
push eax
pop eax
cmp eax, 1
jne L69
push dword [ebp + 8]
push 1
pop ebx
pop edx
add edx, ebx
mov eax, edx
push eax
call recurse
push eax
pop eax
mov [ebp + 8], eax
L69:
push dword [ebp + 8]
pop eax
mov esp, ebp
pop ebp
ret 4



getSqDistance:
push ebp
mov ebp, esp
sub esp, 8
mov ecx, [ebp + 8]
push dword [ecx + vector.a + point.x]
mov ecx, [ebp + 8]
push dword [ecx + vector.b + point.x]
pop ebx
pop edx
sub edx, ebx
mov eax, edx
push eax
mov ecx, [ebp + 8]
push dword [ecx + vector.a + point.x]
mov ecx, [ebp + 8]
push dword [ecx + vector.b + point.x]
pop ebx
pop edx
sub edx, ebx
mov eax, edx
push eax
pop ebx
pop eax
imul ebx
push eax
mov ecx, [ebp + 8]
push dword [ecx + vector.a + point.y]
mov ecx, [ebp + 8]
push dword [ecx + vector.b + point.y]
pop ebx
pop edx
sub edx, ebx
mov eax, edx
push eax
mov ecx, [ebp + 8]
push dword [ecx + vector.a + point.y]
mov ecx, [ebp + 8]
push dword [ecx + vector.b + point.y]
pop ebx
pop edx
sub edx, ebx
mov eax, edx
push eax
pop ebx
pop eax
imul ebx
push eax
pop ebx
pop edx
add edx, ebx
mov eax, edx
push eax
pop eax
mov [ebp - 4], eax
push dword [ebp - 4]
pop eax
mov esp, ebp
pop ebp
ret 4



getBean:
push ebp
mov ebp, esp
sub esp, 12
pusha
push 8
call malloc
add esp, 4
mov [ebp - 4], eax
popa
push 0
pop eax
mov ecx, [ebp - 4]
mov [ecx + firstClass.flag], eax
push 6
pop eax
mov ecx, [ebp - 4]
mov [ecx + firstClass.num], eax
mov ecx, [ebp - 4]
push dword [ecx + firstClass.flag]
push dword [globIntVar]
push 0
pop ebx
pop edx
cmp edx, ebx
je  L197
mov eax, 0
jmp L198
L197:
mov eax, 1
L198:
push eax
pop ebx
pop edx
cmp edx, 1
jne L185
cmp ebx, 1
jne L185
mov eax, 1
jmp L186
L185:
mov eax, 0
L186:
push eax
pop eax
mov [ebp - 8], eax
push dword [ebp - 8]
pop eax
cmp eax, 1
jne L209
mov ecx, [ebp - 4]
push dword [ecx + firstClass.flag]
push int_format
call printf
add esp, 8
jmp L210
L209:
mov ecx, [ebp - 4]
push dword [ecx + firstClass.num]
push int_format
call printf
add esp, 8
L210:
push dword [ebp - 4]
push 8
call malloc
add esp, 4
pop ebx
push 8
push ebx
push eax
call memcpy
add esp, 12
push eax
pop eax
pusha
push dword [ebp - 4]
call free
add esp, 4
popa
mov esp, ebp
pop ebp
ret



strread:
push ebp
mov ebp, esp
sub esp, 8
pusha
push 256
call malloc
add esp, 4
mov [ebp - 4], eax
push dword [ebp - 4]
push str_format
call scanf
add esp, 8
popa
push dword [ebp - 4]
push 256
call malloc
add esp, 4
pop ebx
push ebx
push eax
call strcpy
add esp, 8
push eax
pop eax
pusha
push dword [ebp - 4]
call free
add esp, 4
popa
mov esp, ebp
pop ebp
ret



main:
push ebp
mov ebp, esp
sub esp, 40
pusha
call glob_assign
popa
pusha
push 8
call malloc
add esp, 4
mov [ebp - 4], eax
popa
push tmp1
pop eax
push eax
push 256
call malloc
add esp, 4
mov edx, eax
pop eax
push eax
push edx
call strcpy
add esp, 8
mov ecx, [ebp - 4]
mov [ecx +map.value], edx
pusha
push eax
push edx
call strcpy
add esp, 8
popa
push 666
pop eax
mov ecx, [ebp - 4]
mov [ecx + map.index], eax
mov ecx, [ebp - 4]
push dword [ecx + map.index]
push int_format
call printf
add esp, 8
push tmp2
push str_format
call printf
add esp, 8
mov ecx, [ebp - 4]
push dword [ecx + map.value]
push str_format
call printf
add esp, 8
push tmp3
push str_format
call printf
add esp, 8
pusha
push 8
call malloc
add esp, 4
mov [ebp - 8], eax
popa
push dword [ebp - 4]
pop eax
mov ecx, [ebp - 8]
mov edx, [eax  + map.index]
mov [ecx  + map.index], edx
push eax
push 256
call malloc
add esp, 4
mov ebx, eax
pop eax
mov ecx, [ebp - 8]
mov edx, [eax  + map.value]
mov [ecx  + map.value], ebx
pusha
push edx
push ebx
call strcpy
add esp, 8
popa
mov ecx, [ebp - 8]
push dword [ecx + map.index]
push int_format
call printf
add esp, 8
push tmp4
push str_format
call printf
add esp, 8
mov ecx, [ebp - 8]
push dword [ecx + map.value]
push str_format
call printf
add esp, 8
push tmp5
push str_format
call printf
add esp, 8
push tmp6
pop eax
mov ecx, [ebp - 4]
pusha
push eax
push dword [ecx + map.value]
call strcpy
add esp, 8
popa
push 667
pop eax
mov ecx, [ebp - 4]
mov [ecx + map.index], eax
mov ecx, [ebp - 4]
push dword [ecx + map.index]
push int_format
call printf
add esp, 8
push tmp7
push str_format
call printf
add esp, 8
mov ecx, [ebp - 4]
push dword [ecx + map.value]
push str_format
call printf
add esp, 8
push tmp8
push str_format
call printf
add esp, 8
mov ecx, [ebp - 8]
push dword [ecx + map.index]
push int_format
call printf
add esp, 8
push tmp9
push str_format
call printf
add esp, 8
mov ecx, [ebp - 8]
push dword [ecx + map.value]
push str_format
call printf
add esp, 8
push tmp10
push str_format
call printf
add esp, 8
pusha
push 16
call malloc
add esp, 4
mov [ebp - 12], eax
popa
push dword [ebp - 4]
pop eax
mov ecx, [ebp - 12]
mov edx, [eax  + map.index]
mov [ecx + vecmap.x2 + map.index], edx
push eax
push 256
call malloc
add esp, 4
mov ebx, eax
pop eax
mov ecx, [ebp - 12]
mov edx, [eax  + map.value]
mov [ecx + vecmap.x2 + map.value], ebx
pusha
push edx
push ebx
call strcpy
add esp, 8
popa
push dword [ebp - 8]
pop eax
mov ecx, [ebp - 12]
mov edx, [eax  + map.index]
mov [ecx + vecmap.x1 + map.index], edx
push eax
push 256
call malloc
add esp, 4
mov ebx, eax
pop eax
mov ecx, [ebp - 12]
mov edx, [eax  + map.value]
mov [ecx + vecmap.x1 + map.value], ebx
pusha
push edx
push ebx
call strcpy
add esp, 8
popa
pusha
push 8
call malloc
add esp, 4
mov [ebp - 16], eax
popa
push 0
pop eax
mov ecx, [ebp - 16]
mov [ecx + point.y], eax
push 0
pop eax
mov ecx, [ebp - 16]
mov [ecx + point.x], eax
pusha
push 8
call malloc
add esp, 4
mov [ebp - 20], eax
popa
push 4
pop eax
mov ecx, [ebp - 20]
mov [ecx + point.y], eax
push 3
pop eax
mov ecx, [ebp - 20]
mov [ecx + point.x], eax
pusha
push 16
call malloc
add esp, 4
mov [ebp - 28], eax
popa
push dword [ebp - 16]
pop eax
mov ecx, [ebp - 28]
mov edx, [eax  + point.x]
mov [ecx + vector.b + point.x], edx
mov ecx, [ebp - 28]
mov edx, [eax  + point.y]
mov [ecx + vector.b + point.y], edx
push dword [ebp - 20]
pop eax
mov ecx, [ebp - 28]
mov edx, [eax  + point.x]
mov [ecx + vector.a + point.x], edx
mov ecx, [ebp - 28]
mov edx, [eax  + point.y]
mov [ecx + vector.a + point.y], edx
pusha
push 8
call malloc
add esp, 4
mov [ebp - 24], eax
popa
mov ecx, [ebp - 28]
push ecx
pop eax
mov ecx, [ebp - 24]
mov edx, [eax  + vector.a + point.x]
mov [ecx  + point.x], edx
mov ecx, [ebp - 24]
mov edx, [eax  + vector.a + point.y]
mov [ecx  + point.y], edx
push dword [ebp - 24]
pop eax
mov ecx, [ebp - 28]
mov edx, [eax  + point.x]
mov [ecx + vector.b + point.x], edx
mov ecx, [ebp - 28]
mov edx, [eax  + point.y]
mov [ecx + vector.b + point.y], edx
push tmp11
push str_format
call printf
add esp, 8
mov ecx, [ebp - 28]
push dword [ecx + vector.a + point.x]
push int_format
call printf
add esp, 8
push tmp12
push str_format
call printf
add esp, 8
mov ecx, [ebp - 28]
push dword [ecx + vector.a + point.y]
push int_format
call printf
add esp, 8
push tmp13
push str_format
call printf
add esp, 8
mov ecx, [ebp - 28]
push dword [ecx + vector.b + point.x]
push int_format
call printf
add esp, 8
push tmp14
push str_format
call printf
add esp, 8
mov ecx, [ebp - 28]
push dword [ecx + vector.b + point.y]
push int_format
call printf
add esp, 8
push tmp15
push str_format
call printf
add esp, 8
push tmp16
pop eax
pusha
push 256
call malloc
add esp, 4
mov [ebp - 32], eax
popa
pusha
push eax
push dword [ebp - 32]
call strcpy
add esp, 8
popa
push tmp17
pop eax
pusha
push 256
call malloc
add esp, 4
mov [ebp - 36], eax
popa
pusha
push eax
push dword [ebp - 36]
call strcpy
add esp, 8
popa
push dword [ebp - 32]
push dword [ebp - 36]
call strcmp
add esp, 8
mov ebx, eax
cmp ebx, 0
je L615
mov eax, 0
jmp L616
L615:
mov eax, 1
L616:
push eax
pop eax
cmp eax, 1
jne L607
push tmp18
push str_format
call printf
add esp, 8
push dword [ebp - 32]
push dword [ebp - 36]
push 256
call malloc
add esp, 4
pop ebx
pop edx
push edx
push eax
call strcpy
add esp, 8
push ebx
push eax
call strcat
add esp, 8
push eax
push tmp19
push 256
call malloc
add esp, 4
pop ebx
pop edx
push edx
push eax
call strcpy
add esp, 8
push ebx
push eax
call strcat
add esp, 8
push eax
push str_format
call printf
add esp, 8
L607:
push 0
pop eax
mov ecx, [ebp - 4]
mov edx, [ecx  + map.value]
pusha
push edx
call free
add esp, 4
popa
pusha
push dword [ebp - 4]
call free
add esp, 4
popa
mov ecx, [ebp - 8]
mov edx, [ecx  + map.value]
pusha
push edx
call free
add esp, 4
popa
pusha
push dword [ebp - 8]
call free
add esp, 4
popa
pusha
push dword [ebp - 28]
call free
add esp, 4
popa
mov ecx, [ebp - 12]
mov edx, [ecx  + vecmap.x1 + map.value]
pusha
push edx
call free
add esp, 4
popa
mov ecx, [ebp - 12]
mov edx, [ecx  + vecmap.x2 + map.value]
pusha
push edx
call free
add esp, 4
popa
pusha
push dword [ebp - 12]
call free
add esp, 4
popa
pusha
push dword [ebp - 16]
call free
add esp, 4
popa
pusha
push dword [ebp - 36]
call free
add esp, 4
popa
pusha
push dword [ebp - 20]
call free
add esp, 4
popa
pusha
push dword [ebp - 24]
call free
add esp, 4
popa
pusha
push dword [ebp - 32]
call free
add esp, 4
popa
mov esp, ebp
pop ebp
ret



power:
push ebp
mov ebp, esp
sub esp, 8
push 1
pop eax
mov [ebp - 4], eax
push dword [ebp + 8]
push 0
pop ebx
pop edx
cmp edx, ebx
ja  L665
mov eax, 0
jmp L666
L665:
mov eax, 1
L666:
push eax
pop eax
cmp eax, 1
jne L659
L660:
push dword [ebp + 8]
push 2
pop ebx
pop eax
mov edx, 0
idiv ebx
mov eax, edx
push eax
push 1
pop ebx
pop edx
cmp edx, ebx
je  L681
mov eax, 0
jmp L682
L681:
mov eax, 1
L682:
push eax
pop eax
cmp eax, 1
jne L673
push dword [ebp - 4]
push dword [ebp + 12]
pop ebx
pop eax
imul ebx
push eax
pop eax
mov [ebp - 4], eax
L673:
push dword [ebp + 12]
push dword [ebp + 12]
pop ebx
pop eax
imul ebx
push eax
pop eax
mov [ebp + 12], eax
push dword [ebp + 8]
push 2
pop ebx
pop eax
mov edx, 0
idiv ebx
push eax
pop eax
mov [ebp + 8], eax
push dword [ebp + 8]
push 0
pop ebx
pop edx
cmp edx, ebx
ja  L721
mov eax, 0
jmp L722
L721:
mov eax, 1
L722:
push eax
pop eax
cmp eax, 1
je L660
L659:
push dword [ebp - 4]
pop eax
mov esp, ebp
pop ebp
ret 8



section .data
int_format dd "%d", 10, 0
str_format dd "%s", 10, 0
globIntVar: dd 0
globStringVarWithInit: dd "It is global string", 0
globBoolVar: dd 0
tmp1: dd "first", 0
tmp2: dd " ", 0
tmp3: dd " ", 0
tmp4: dd " ", 0
tmp5: dd " ", 0
tmp6: dd "second", 0
tmp7: dd " ", 0
tmp8: dd " ", 0
tmp9: dd " ", 0
tmp10: dd " ", 0
tmp11: dd "v.a.x = ", 0
tmp12: dd "v.a.y = ", 0
tmp13: dd "v.b.x = ", 0
tmp14: dd "v.b.y = ", 0
tmp15: dd "     ", 0
tmp16: dd "hello, ", 0
tmp17: dd "hello, ", 0
tmp18: dd " ", 0
tmp19: dd "fuckers! ", 0

section .bss
motherfucker: resb firstClass.size
